FORMAT: 1A
HOST: http://api.samplehost.com

#Worker-Service

This is the documentation of the REST-interface of the Worker-Service.

### Repositories
Check out the source-code of the worker-service at [github](https://github.com/coolcrowd/worker-service) or learn about the object-service and the general architecture at the [object-service](https://github.com/coolcrowd/object-service). All the json-objects used for the communication are defined as protobuf-files located at the [spec-repository](https://github.com/coolcrowd/spec).

### testing
Useful for testing is the dummy-platform:
The dummy platform does not pay any workers and identifies worker by their email, it also displays calibrations. It is used for testing functionality depending on crowdworking-platforms. It is recommended to use the docker-compose files [here](https://github.com/coolcrowd/object-service/tree/master/image/compose) for setup.<br>
<br>
<br>

### Protocol
Due to the nature of protobuf, integer fields always appear as zero when not initialized. This also means that when you sugmit a rating without a rating field, the rating counts as zero.

## Authorization
The Worker-Service uses [JWT](https://jwt.io) to authorize some rest-calls and indentify the users. It uses the Bearer schema, the caller has to provide the `Authentication` header with the value `Bearer <Token>`. The token can be persisted and reused later in another session.

## Group View

Resources specifying what to display the worker.

## Experiments [/experiments/{platform}]

The /experiments command is used to retrieve all running experiments for the platform.

The response:

 Field  | Type   | Description
-------| ---- | -----------
 experiments | (array[Experiment]) | the running experiments, each experiment consists of an id, title and description

+ Parameters
    + platform: `dummy` (required, string) - represents the platform the worker is working on.

### Get the running Experiments [GET]

+ Response 200 (application/json)
    This is an example of the response. Please read the following segments to understand the whole process.

    + Body

            {
              "experiments": [
                {
                  {
                    "id" : 1,
                    "title" : "example title",
                    "description": "example description"
                  },
                  {
                    "id" : 3,
                    "title" : "another experiment",
                    "description": "with another description"
                  }
                }
              ]
            }

## Next [/next/{platform}/{experiment}{answer,rating}]

The /next command is crucial for the worker-service. It instructs the requestor what to display next and consequently what the worker should be working on. Every response has a type which describes the result. There are 5 different types: **FINISHED, ANSWER, RATING, CALIBRATION** and **EMAIL**. **FINISHED** means there are no more assignments left, the worker can be redirected to the crowdworking-platform. **ANSWER** represents an Creative-Task, the client is expected to present the experiment to the worker and let the worker create one or more answers to it. **RATING** should present the experiment and display the answers of other workers. The worker can now rate these answers. **CALIBRATION** expects the client to present the returned questions and let him choose the answer from pre-defined fields. **EMAIL** represents the need of an email-address from a worker. Additional query-parameters may be required for certain platforms.

::: note
As soon as the client has obtained a jwt, it is expected to pass it to the /next call.
:::

::: note
The worker-service expects the client to take care of checking that the user works at least on one ANSWER or RATING tasks before he skips all the others. This means that if the client has at least one of the query-parameter answer or rating set to skip and gets the type FINISHED from the worker-serice he has remember whether the worker submitted an answer or rating. If he has not, the client has to try the /next command again without the query-parameter answer or rating. If the returning type is not FINISHED, the client should notify the worker that he has to work on at least one of the ANSWER or RATING tasks and act according to the returned type.
:::

The response:

 Field  | Type   | Description
-------| ---- | -----------
 authorization | (string) | the jwt
 type | (enum[string], required) | The type of the view, this field is always set and determines what other fields are also set.
 title | (string) | The title of the experiment
 description | (string) | the description of the experiment
 answer_reservations | (array[number]) | the reservation for each answer the worker is allowed to submit
 answersToRate | (array[View_Answer]) | the answers the worker can rate
 answerType | (string) | the answer type, if set this means the worker submits a link pointing to a resource with the mime-type answerType
 ratingOptions | (array[View_RatingOption]) | the options to rate one answer
 constraints | (array[View_Constraint]) | the constraints which the worker must check or avoid being violated
 pictures | (array[View_Picture]) | the pictures if the experiment contains any
 calibrations | (array[View_Calibration]) | the calibrations the worker must answer

The worker-service supports multiple sessions. Depending on the time-limit of the platform and platform-specific properties, the worker can work on an experiment, finish and later start again.

The protobuf definition of the resource can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/view.proto);

Full details about the types in the next segments:

+  Attributes (View)

+ Parameters
    + platform: `dummy` (required, string) - represents the platform the worker is working on.
    + experiment: 13 (required, number) - the experiment the worker is currently working on.
    + answer: skip (optional, skip) - passed if the worker wants to skip the answer-task
    + rating: skip (optional, skip) - passed if the worker wants to skip the rating-task

### Get next view [GET]

+ Response 200 (application/json)
    This is an example of the response. Please read the following segments to understand the whole process.

    + Body

            {
              "authorization" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ",
              "type": "CALIBRATION",
              "calibrations": [
                {
                  "question": "did you attend to an university?",
                  "answerOptions": [
                    {
                      "option" : "yes",
                      "id" : 23
                    },
                    {
                      "option" : "no",
                      "id" : 24
                    }
                  ],
                  "id": 4,
                }
              ]
            }

## Example: Next EMAIL [/next/example/13?exampledependent=13]

Scenario: the platform needs an email from his workers and it is the first time the worker is working on our framework. Therefore the example-platform will not find us in the database and the worker-service proceeds to respond with the *EMAIL* type. The query-parameter `exampledependent=13` represents an platform-dependent query-parameter, that will be passed further into the system.

Expected Behavior: Ask the Worker for his email-address and submit it. Then call /next with the jwt obtained through the submit email request.

### next with type EMAIL [GET]

+ Response 200 (application/json)
    The type is EMAIL, no additional fields are set.

    + Body

            {
              "type": "EMAIL"
            }

## Example: Next CALIBRATION [/next/example/13?exampledependent=15]

Scenario: the example-platform has the displaying of calibrations activated, can identify the worker from the passed, platform-dependent query parameter and the worker has already worker with our framework. The example-platform now finds the matching worker-id in the database and returns it to the worker-service. The worker-service now notices that the worker has not answered all the calibrations, so it returns the type *CALIBRATION* and an array of calibrations as *calibrations*. The query-parameter `exampledependent=15` represents an platform-dependent query-parameter, that will be passed further into the system.

Expected Behavior: Let the worker choose his answers for all the calibrations and submit them with /calibrations, then call /next with the worker-id as an parameter. Calling /next without submitting all the calibrations will result in the type *CALIBRATION*, where the field *calibrations* holds all the remaining calibrations.

### next with type CALIBRATION [GET]

+ Response 200 (application/json)
    The type is CALIBRATION, also the field calibrations is added to specify the calibrations to answer. Furthermore the field worker-id is set, and the client is expected to hold it in memory.

    + Body

            {
              "workerId" : 3,
              "type": "CALIBRATION",
              "calibrations": [
                {
                  "question": "did you attend to an university?",
                  "answerOptions": [
                    {
                      "option" : "yes",
                      "id" : 23
                    },
                    {
                      "option" : "no",
                      "id" : 24
                    }
                  ],
                  "id": 4,
                  }
              ]
            }

## Example: Next ANSWER [/next/example/13?exampledependent=66]

Scenario: The worker has completed all calibrations and the worker-service decides that he can work on an creative-Task. So it returns the type *ANSWER* and all the relevant information about the experiment(*title*, *description*). The experiment has some pictures and some constraints, so additionally it also adds these. The worker-service also returns answer_reservations, which specifies how many creative-answers for the worker are left and the reservation id for each answer. The worker-service also passes an *answerType*.

Expected Behavior: The client is expected to render the title, description and the pictures. Additionally the worker has to be warned about the constraints. The worker has now the chance to create up to maxAnswersToGive answers and the client should submit them via /answers. After submitting the client should call /next. When the worker-service passes an *answerType*, the answer to the creative task is a url, pointing to a resource with the mime-type of *answerType*. It is expected that the client verifies that the answer is indeed a valid url, but it is not expected that it checks whether the mime-type matches the resource the url is pointing to.

### next with type ANSWER  [GET]

+ Response 200 (application/json)
    The type is ANSWER, which requires the fields title, description and answer_reservations to be set. Furthermore the fields pictures, answerType and constraints are set.

    + Body

            {
              "type": "ANSWER",
              "title": "Find a specific Picture.",
              "description": "This is the description of the example-task. It specifies what the worker has to do.",
              "answer_reservations" : [13, 66, 67, 68],
              "answerType" : "images"
              "pictures": [
                  {
                    "url": "http://example.picture.jpg",
                    "urlLicense": "http://www.source.de"
                  }
              ],
              "constraints" : [
                {
                  "id" : 5,
                  "name" : "The tweet must not be racist."
                }
              ]
            }

## Example: Next RATING [/next/example/13]

Scenario: The example-platform does not render calibrations and the worker-service decides that the worker should do a rating-task. Therefore the type is *RATING* and all the relevant information about the experiment(*title*, *description*) is set. The experiment has also some constraints, so the worker-service passes them too via the field *constraints*. The worker-service also returns *answersToRate* and *ratingOptions*. The field *pictures* may be set if the experiment provides pictures, but in this example is not. Please compare Next Answer for an example where the field pictures is set.

Expected Behavior: The client is expected to render the title and the description of the experiment. Additionally the calibrations have to be placed prominently. The worker has now the chance to rate the passed answers (answersToRate). For each answer he can choose *one* of the rating-options (ratingOptions) and specify *which constraints* (if any) got violated. One rating-option is a description/value pair. The description should be rendered for the worker and the value represents the chosen rating. The worker should also be encouraged to submit a feedback containing a critique of the answer. The client should submit them via /ratings. After submitting the client should call /next.

### next with type RATING [GET]

+ Response 200 (application/json)
    The type is RATING, which requires the fields title, description, answersToRate and ratingOptions to be set. Furthermore the field constraints is set.

    + Body

            {
              "type": "RATING",
              "title": "Come up with a joke!",
              "description": "Come up with a joke about the following situation....",
              "answers": [
                  {
                    "id": 12,
                    "answersID" : 15,
                    "answer": "Lame joke"
                  },
                  {
                    "id": 18,
                    "answersID" : 87,
                    "answer": "This is another lame joke"
                  }
                ],
                "constraints": [
                  {
                    "id" : 55,
                    "name" : "the joke must not be racist."
                  }
              ],
              "ratingOptions": [
                  {
                    "value" : 0,
                    "description" : "bad"
                  },
                  {
                    "value" : 10,
                    "description" : "good"
                  }
              ]
            }

## Example: Next FINISHED [/next/example/13?rating=skip]

Scenario: The worker-service requested 5 ratings from the worker, but the worker only worked on 3. In this situation the query-parameter rating=skip can be passed, to signal that the worker wants to skip further ratings. Otherwise every new /next call would result in the type RATING with 2 answers to rate. The worker-service now detects that there is nothing else to do, so he returns the type FINISHED.

Expected Behavior: The client is expected to notice the worker that his work is finished and redirects him to the crowdworking-platform.

### next with type FINISHED [GET]

+ Response 200 (application/json)
    The type is FINISHED, which requires no other field to be set.

    + Body

            {
              "type": "FINISHED"
            }

## Preview [/preview/{experiment}]

this call returns a preview of the experiment.

The protobuf definition of the resource can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/view.proto);

Please look at the /next command to get a better understanding of the /preview command.

The response:

 Field | Type   | Description
-------|--------|------
 type | (string, required) | The type of the view, always ANSWER
 title | (string, required) | The title of the experiment
 description | (string, required) | the description of the experiment
 pictures | (array[View_Picture]) | the pictures if present
 constraints | (array[View_Constraint]) | the constraints if present

+ Attributes (object)
  + type: ANSWER (string, required) - The type of the view, always ANSWER
  + title: Mean Tweet (string, required) - The title of the experiment
  + description: Description of Mean Tweet Assignment (string, required) - the description of the experiment
  + pictures (array[View_Picture]) - the pictures if present
  + constraints (array[View_Constraint]) - the constraints if present

+ Parameters
    + experiment: 13 (required, number) - the experiment the worker is currently working on.

### Get preview [GET]

+ Response 200 (application/json)

    + Body

            {
              "type": "ANSWER",
              "title": "Find a specific Picture.",
              "description": "This is the description of the example-task. It specifies what the worker has to do.",
              "answerType" : "images"
              "pictures": [
                  {
                    "url": "http://example.picture.jpg",
                    "urlLicense": "http://www.source.de"
                  }
              ],
              "constraints" : [
                {
                  "id" : 5,
                  "name" : "The tweet must not be racist."
                }
              ]
            }

## Group Submit

Resources specifying the submitting of information from the worker.

## Email [/emails/{platform}]

Some crowd-platforms require the email-address of the worker. Most of the time this is required because the platform has no native payment system and has to default to the built-in email based payment system in CrowdControl. If a worker wishes to not disclose his email-address, pass an empty string. This has the consequence that CrowdControl is unable to pay the worker. The response is a json-object containing the jwt.

The protobuf definition of the resource can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/email.proto);

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/email_answer.proto);

The message:
 Field | Type   | Description
-------|--------|--------------
 email | (string, required) | The email address to submit (or empty if the worker wishes to not disclose his email-address)
 platform_parameters |(array[PlatformParameter]) | optional platform-dependent parameters

+ Attributes (object)
    + email: email.worker@example.org (string, required) - The email address to submit (or empty if the worker wishes to not disclose his email-address)
    + platform_parameters: "platform_parameters" : ["key" : "exampleKey","value" : ["examplevalue1", "examplevalue2"]] (array[PlatformParameter]) - optional platform-dependent parameters

+ Parameters
    + platform: `dummy` (required, string) - represents the platform the worker is working on.

### Submit an email-address [POST]

+ Request (application/json)
      + Body

            {
              "email": "example.address@example.org"
              "platform_parameters" : [
                "key" : "exampleKey",
                "value" : ["examplevalue1", "examplevalue2"]
              ]
            }

+ Response 201 (application/json)

      + Body

              {
                 "authorization": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
              }

+ Request (application/json)

        {"email": ""}

+ Response 201 (application/json)

      + Body

              {
                 "authorization": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
              }

## Answer [/answers]

This command is used to submit a creative answer. A creative answer is the work on the /next with the type ANSWER.

::: note
This command requires the jwt set.
:::

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/answer.proto);

The message:

 Field | Type   | Description
-------|------|---------------------------
 answer | (string, required) | the answer of the worker to a creative-view.
 experiment | (number, required) | represents the experiment the worker is currently working on.
 reservation | (number, required) | the reservation for the answer

+ Attributes (object)
    + answer: Why did the chicken cross the road? (string, required) - the answer of the worker to a creative-view.
    + experiment: 13 (number, required) - represents the experiment the worker is currently working on.
    + reservation: 55 (number, required) - the reservation of the answer

### Submit a creative answer [POST]

+ Request (application/json)

        {"answer" : "http://www.example.org/image.jpg", "experiment" : 33, "reservation": 44}

+ Response 200

+ Request (application/json)

        {"answer" : "The picture contains 3 red circles.", "experiment" : 18, "reservation": 45}

+ Response 200

## RATING [/ratings]

This command is used to submit a rating. A rating is the work on the /next with the type RATING.

::: note
This command requires the jwt set.
:::

The protobuf definition of the rating can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/rating.proto);

The message:

| Field | Type   | Description               |
|-------|--------------------------|---------------------------|
| ratingId | (number, required) | the id of the rating, the id from answersToRate |
| rating | (number) | the value from the chosen key value-pair from ratingOptions. If the rating gets ommited the worker-service assumes a rating of zero. |
| experiment | (number, required) | the experiment currently worked on |
| answerId | (number, required) | he answerId from the answersToRate |
| feedback | (string) | feedback for the worker rated |
| constraints | (array[number]) | the ids of the constraints violated |


+ Attributes (object)
    + ratingId: 52 (number, required) - the id of the rating, the id from answersToRate
    + rating: 18 (number, required) - the value from the chosen key value-pair from ratingOptions
    + experiment: 11 (number, required) - the experiment currently worked on
    + answerId: 13 (number, required) - the answerId from the answersToRate
    + feedback: the knock knock joke was not easy to understand (string) - feedback for the worker rated
    + constraints: [11,28] (array[number]) - the ids of the constraints violated

### Submit a rating [POST]

+ Request (application/json)

        {
          "rating" : 0,
          "experiment" : 15,
          "answerId" : 22244,
          "feedback" : "this is racist!",
          "constraints" : [13, 18]
        }

+ Response 200

+ Request (application/json)

        {
          "rating": 30,
          "experiment": 15,
          "answerId": 22244
        }

+ Response 200

## Calibration [/calibrations]

This command is used to submit a calibration. A calibration is the work on the /next with the type CALIBRATION.

::: note
This command requires the jwt set.
:::

The protobuf definition of the calibration can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/calibration.proto);

The message:

| Field | Type   | Description               |
|-------|------|--------------------------|
| answerOption | (number, required) | the id of the answerOption chosen from the calibration |


+ Attributes (object)
    + answerOption: 18 (number, required) - the id of the answerOption chosen from the calibration

### Submit a calibration [POST]

+ Request (application/json)

         {
           "answerOption": 29
         }

+ Response 200

+ Request (application/json)

        {
          "answerOption": 88
        }

+ Response 200




# Data Structures

## View (object)
+ authorization: xxx.xxxx.xxx (number) - The jwt-token
+ type: FINISHED, ANSWER, RATING, CALIBRATION, EMAIL (enum[string], required) - The type of the view, this field is always set and determines what other fields are also set.

+ title: Mean Tweet (string) - The title of the experiment
+ description: Description of Mean Tweet Assignment (string) - the description of the experiment
+ maxAnswersToGive: 13 (number) - the maximum number of answers the worker is allowed to submit
+ answersToRate (array[View_Answer]) - the answers the worker can rate
+ answerType: "image" (string) - the answer type, if set this means the worker submits a link pointing to a resource with the mime-type answerType
+ ratingOptions (array[View_RatingOption]) - the optionas to rate one answer
+ constraints (array[View_Constraint]) - the contraints which the worker must check or avoid bein violated
+ pictures (array[View_Picture]) - the pictures if the experiment contains any
+ calibrations (array[View_Calibration]) - the calibrations the worker must answer

## View_Answer (object)
+ id: 3 (number, required)
+ answerId: 14 (number, required)
+ answer: Sauron kills everybody. The end. (string, required)

## View_RatingOption (object)
+ value: 16 (number, required)
+ description: good (string, required)

## View_Constraint (object)
+ id: 1874 (number, required) - the id of the calibration
+ name: Racism makes pandas sad. No racism. (string, required) - the question
+ answer_options: (array[View_Constraint_Answer_Option]) - the answer-options

## View_Constraint_Answer_Option (object)
+ id: 1874 (number, required) - the id of the answer-option
+ option: yes. (string, required) - the display-value of the option

## View_Picture (object)
+ url: pictures.example.org/1.jpeg (string, required)
+ urlLicense: picture.example.org/my+happy+holiday (string)

## View_Calibration (object)
+ question: do you plan on cheating on your work? (string, required)
+ answerOptions:[{"option":"yes", "id":3}] (array[View_CalibrationAnswerOption], required)

## View_CalibrationAnswerOption (object)
+ option: yes (string, required)
+ id: 3 (number, required)

## Email_Submit (object)
+ email: test@example.org (string, required) - the email address to submit
+ platform_parameters: "platform_parameters" : ["key" : "exampleKey","value" : ["examplevalue1", "examplevalue2"]] (array[PlatformParameter]) - the platform-parameters

## PlatformParameter (object)
+ key : test (string, required) - the key of the platform
+ values : ["value1", "value2"] (array[string], required)
