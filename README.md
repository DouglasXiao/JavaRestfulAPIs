# JavaRestfulAPIs

This system use Spark framework to handle restful APIs, and the language is Java.

How to use this system:
1. Download the project from Git repository and open in IntelliJ
2. Click start running in Appointments class, be careful that your port 4567 is not occupied
3. Open postman, send request like: GET "/appointments/123456/2018-07-22/free", POST "/appointments/123456/2018-07-22/10:30/MaDongMei",
DELETE ... ,etc. You can see the responses with JSON format.



The system is responsible for scheduling medical appointments over the internet. In order to do so it exposes a RESTful API that allows you to get a list of available slots for a given day, request a slot to be reserved and cancel a reserved slot.  All calls are made with a valid token id given previously to the client by other part of the system not relevant to this piece.

See the API definition below:
GET /appointments/<token-id>/<date>/free – get a list of free slots for a given date
 
Answer:
{
       slots: [“10:00”, “10:30”,…]
}
 
Or
 
{
       slots: []
}
 
If no slots are available
 
POST /appointments/<token-id>/<date>/<time>/<patient name> - request a slot to be reserved on the date and time for the patient name.
 
Answer:
{
       appointmentId: xxx
}
 
If it was successful.
Or
 
{
       error: “Unable to reserve the appointment”
}
 
 
DELETE /appointments/<token-id>/<appointment-id> - deletes an appointment
 
Answer:
{
       “success”
}
 
Or
{
error: “Unable to cancel the appointment”
}

When running the unit tests, click to run the cases separately, to avoid running batch with connection problem
