networking structure for questions: 

- Notif sent to client and facilitator that match has begun.
- Client and server both request respective question data, which is sent to them.
- When a client submits an answer, send it to the server for verif.
- Notify the clients and facilitator of the result.
  - if the client is not the one who sent the result, DO NOT expose this result as a variable.
- Continue until facilitator recognizes that all users have answered the question.
- Facilitator informs server of this, sending relevant scoring data.
- Server responds by sending out new questiondata.