FORMAT: 1A
HOST: http://api.samplehost.com

#Worker-Service


This is the documentation of the REST-interface of the Worker-Service.

### Repositories
Check out the source-code of the worker-service at [github](https://github.com/coolcrowd/worker-service) or learn about the object-service and the general architecture at the [object-service](https://github.com/coolcrowd/object-service). All the json-objects used for the communication are defined as protobuf-files located at the [spec-repository](https://github.com/coolcrowd/spec).

### testing
Useful for testing is the dummy-platform:
The dummy platform does not pay any workers and identifies worker by their email, it also displays calibrations. It is used for testing functionality depending on crowdworking-platforms.<br>
<br>
<br>
## Group View

Resources specifying what to display the worker.

## Next [/next/{platform}/{experiment}{?worker,answer,rating}]

The /next commant is crucial for the worker-service. It instructs the requestor what to display next and consquentyl what the worker should be working on. Every response has a type which describes the result. There are 5 different types: **FINISHED, ANSWER, RATING, CALIBRATION** and **EMAIL**. **FINISHED** means there are no more assignments left, the worker can be redirected to the crowdworking-platform. **ANSWER** represents an Creative-Task, the client is expected to present the experiment to the worker and let the worker create one or more answers to it. **RATING** should present the experiment and display the answers of other workers. The worker can now rate these answers. **CALIBRATION** expects the client to present the returned questions and let him choose the answer from pre-defined fields. **EMAIL** represents the need of an email-address from a worker. Additional query-parameters may be required for certain platforms.

The client may choose to persist the worker-id and pass it when the worker starts working on our framework again, on the same or on an different experiment. The worker-id identifies the worker, belongs to the worker and does not change (event between differen experiments).

::: note
The worker-service expects the client to take care of checking that the user works at least on one ANSWER or RATING taks before he skips all the others. This means that if the client has at least one of the query-parameter answer or rating set to skip and gets the type FINISHED from the worker-serice he has remember whether the worker submitted an answer or rating. If he has not, the client has to try the /next command again without the query-parameter answer or rating. If the returning type is not FINISHED, the client should notify the worker that he has to work on at least one of the ANSWER or RATING tasks and act according to the returned type.
:::

The worker-service supports mutiple sessions. Depending on the time-limit of the platform and platform-specific properties, the worker can work on an experiment, finish and later start again.

The protobuf definition of the resource can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/view.proto);

Full details about the types in the next segements:

+  Attributes (View)

+ Parameters
    + platform: `dummy` (required, string) - represents the platform the worker is working on.
    + experiment: 13 (required, number) - the experiment the worker is currently working on.
    + worker: 15 (optional, number) - the workerId if the client knows about them
    + answer: skip (optional, skip) - passed if the worker wants to skip the answer-task
    + rating: skip (optional, skip) - passed if the worker wants to skip the rating-task

### Get next view [GET]

+ Response 200 (application/json)
    This is an example of the response. Please read the following segments to understand the whole process.

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

## Example: Next EMAIL [/next/example/13?exampledependent=13]

Scenario: the platform needs an email from his workers and it is the first time the worker is working on our framework. Therefore the example-platform will not find us in the database and the worker-service proceeds to respond with the EMAIL type.

Expected Behaviour: Ask the Worker for his email-address and submit it. Then call /next with the worker-id obtained through the submit email request.

### next with type EMAIL [GET]

+ Response 200 (application/json)
    The type is EMAIL, no additional fields are added.

    + Body

            {
              "type": "EMAIL"
            }

## Example: Next CALIBRATION [/next/example/13?exampledependent=15]

Scenario: the example-platform has the displaying of calibrations activated, can identify the worker from the passed, platform-dependent query parameter and the worker has already worker with our framework. The example-platform now finds the matching worker-id in the database and returns it to the worker-service. The worker-service now notices that the worker has not answered all the calibrations, so it returns the type CALIBRATION.

Expected Behaviour: Let the worker choose his answeres for all the calibrations and submit them with /calibrations, then call /next with the worker-id as an parameter. Calling /next without submitting all the calibrations will result in the type CALIBRATION, where the field calibrations holds all the remaining calibrations.

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

## Example: Next ANSWER [/next/example/13?worker=15&exampledependent=15]

Scenario: The worker with the worker-id 15 has completed all calibrations and the worker-service decides that he can work on an creative-Task. So it returns the type ANSWER and all the relevant information about the experiment(title, description). The experiment has some pictures and some constraints, so additionally it also adds these. The worker-service also returns maxAnswersToGive, which specifies how many creative-answers for the worker are left.

Expected Behaviour: The client is expected to render the title, description and the pictures. Additionally the worker has to be warned about the constraints. The worker has now the chance to create up to maxAnswersToGive answers and the client should submit them via /answers. After submitting the client should call /next.

### next with type ANSWER  [GET]

+ Response 200 (application/json)
    The type is ANSWER, which requires the fields title, description and maxAnswersToGive to be set. Furthermore the fields pictures and constraints are set.

    + Body

            {
              "type": "ANSWER",
              "title": "Mean Tweet",
              "description": "This is the description of the example-task. It specifies what the worker has to do.",
              "maxAnswersToGive" : 3,
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

## Example: Next RATING [/next/example/13?worker=18]

Scenario: The example-platform does not render calibrations and the worker-service decides that the worker should do a rating-task. Therefore the type is RATING and all the relevant information about the experiment(title, description). The experiment has also some constraints, so the worker-service passes them, too. The worker-service also returns answersToRate and ratingOptions.

Expected Behaviour: The client is expected to render the title and the description of the experiment. Additionally the calibrations have to be placed prominentyl. The worker has now the chance to rate the passed answers (answersToRate). For each answer he can choose on of the rating-options (ratingOptions) and specify which constraint (if any) got violated. The worker should also be encouraged to submit a feedback containing a critique of the answer. The client should submit them via /ratings. After submitting the client should call /next.

### next with type RATING [GET]

+ Response 200 (application/json)
    The type is RATING, which requires the fields title, description, answersToRate and ratingOptions to be set. Furthermore the field constraints is set.

    + Body

            {
              "type": "ANSWER",
              "title": "Come up with a joke!",
              "description": "Come up with a joke about the follwing situation....",
              "answers": [
                  {
                    "id": 12,
                    "answer": "Why did the chicken cross the road? Because."
                  },
                  {
                    "id": 18,
                    "answer": "Knock, knock. Who’s there? Not you."
                  }
                ],
                "constraints": [
                  {
                    "id" : 55,
                    "name" : "the joke must not be racist."
                  }
              ]
            }

## Example: Next FINISHED [/next/example/13?worker=22&rating=skip]

Scenario: The worker-service requested 5 ratings from the worker, but the worker only worked on 3. In this situation the query-parameter rating=skip can be passed, to signal that the worker wants to skip further ratings. Otherwise every new /next call would result in the type RATING with 2 answers to rate. The worker-service now detects that there is nothing else to do, so he returnes the type FINISHED.

Expected Behaviour: The client is expected to notice the worker that his work is finished and redirects him to the crowdworking-platform.

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

+ Attributes (object)
  + title: Mean Tweet (string, required) - The title of the experiment
  + description: Description of Mean Tweet Assignment (string, required) - the description of the experiment
  + pictures (array[View_Picture])
  + calibrations (array[View_Calibration])

+ Parameters
    + experiment: 13 (required, number) - the experiment the worker is currently working on.

### Get preview [GET]

+ Response 200 (application/json)

      + Attributes (Preview)

## Group Submit

Resources specifying the submitting of information from the worker.

## Email [/emails/{platform}]

Some crowdplatforms require the email-address of the worker. Most of the time this is required because the platform has no native payment system and has to default to the built-in email based payment system in CrowdControl. If a worker wishes to not disclose his email addres, pass an empty string. This has the consequence that CrowdControl is unable to pay the worker.

The protobuf definition of the resource can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/email.proto);

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/email_answer.proto);

+ Attributes (object)
    + email: email.worker@example.org (string, required) - The email address to submit (or empty if the worker whishes to not disclose his email-address)

+ Parameters
    + platform: `dummy` (required, string) - represents the platform the worker is working on.

### Submit an email-address [POST]

+ Request (application/json)

      +  Attributes (Email)

+ Response 201 (application/json)

      + Body

              {
                 "workerId": 42
              }

+ Request (application/json)

        {"email": ""}

+ Response 201 (application/json)

      + Body

              {
                 "workerId": 1337
              }

## Answer [/answers/{workerId}]

This command is used to submit a creative answer. A creative answer is the work on the /next with the type ANSWER.

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/answer.proto);

+ Attributes (object)
    + answer: Why did the chicken cross the road? (string, required) - the answer of the worker to a creative-view.
    + experiment: 13 (number, required) - represents the experiment the worker is currentyl working on.

+ Parameters
    + workerId: 1882 (required, number) - The worker-id is used by crowdcontrol to identify the individual worker. An worker-id can be obtained by either calling GET /next or if the platform needs an email through POST emails.

### Submit a creative answer [POST]

+ Request (application/json)

      +  Attributes (Answer)

+ Response 200

+ Request (application/json)

        {"answer" : "The picture contains 3 red circles.", "experiment" : 18}

+ Response 200

## RATING [/ratings/{workerId}]

This command is used to submit a rating. A rating is the work on the /next with the type RATING.

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/rating.proto);

+ Attributes (object)
    + rating: 18 (number, required) - the value from the chosen key value-pair from ratingOptions
    + experiment: 11 (number, required) - the experiment currently workerd on
    + answerId: 13 (number, required) - the answer rated
    + feedback: the knock knock joke was not easy to understand (string) - feedback for the worker rated
    + constraints: [11,28] (array[number]) - the ids of the contraints violated

+ Parameters
    + workerId: 1882 (required, number) - The worker-id is used by crowdcontrol to identify the individual worker. An worker-id can be obtained by either calling GET /next or if the platform needs an email through POST emails.

### Submit a rating [POST]

+ Request (application/json)

      +  Attributes (RATING)

+ Response 200

+ Request (application/json)

        {
          "rating": 30,
          "experiment": 15,
          "answerId": 22244
        }

+ Response 200

## Calibration [/calibrations/{workerId}]

This command is used to submit a calibration. A calibration is the work on the /next with the type CALIBRATION.

The protobuf definition of the answer can be viewed [here](https://github.com/coolcrowd/spec/blob/master/workerservice/calibration.proto);

+ Attributes (object)
    + answerOption: 18 (number, required) - the id of the answerOption chosen from the calibration

+ Parameters
    + workerId: 1882 (required, number) - The worker-id is used by crowdcontrol to identify the individual worker. An worker-id can be obtained by either calling GET /next or if the platform needs an email through POST emails.

### Submit a calibration [POST]

+ Request (application/json)

      +  Attributes (RATING)

+ Response 200

+ Request (application/json)

        {
          "rating": 30,
          "experiment": 15,
          "answerId": 22244
        }

+ Response 200




# Data Structures

## View (object)
+ workerId: 42 (number) - The worker-id
+ type: FINISHED, ANSWER, RATING, CALIBRATION, EMAIL (enum[string], required) - The type of the view, this field is always set and determines what other fields are also set.

+ title: Mean Tweet (string) - The title of the experiment
+ description: Description of Mean Tweet Assignment (string) - the description of the experiment
+ maxAnswersToGive: 13 (number)
+ answersToRate (array[View_Answer])
+ ratingOptions (array[View_RatingOption])
+ constraints (array[View_Constraint])
+ pictures (array[View_Picture])
+ calibrations (array[View_Calibration])

## View_Answer (object)
+ id: 3 (number, required)
+ answer: Sauron kills everybody. The end. (string, required)

## View_RatingOption (object)
+ value: 16 (number, required)
+ description: good (string, required)

## View_Constraint (object)
+ id: 1874 (number, required)
+ name: Racism makes pandas sad. No racism. (string, required)

## View_Picture (object)
+ url: pictures.example.org/1.jpeg (string, required)
+ urlLicense: picture.example.org/my+happy+holiday (string)

## View_Calibration (object)
+ question: do you plan on cheating on your work? (string, required)
+ answerOptions:[{"option":"yes", "id":3}] (array[View_CalibrationAnswerOption], required)

## View_CalibrationAnswerOption (object)
+ option: yes (string, required)
+ id: 3 (number, required)
