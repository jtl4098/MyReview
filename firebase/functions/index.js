const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { user } = require("firebase-functions/v1/auth");
// admin.initializeApp();
admin.initializeApp(functions.config().firebase);

exports.androidPushNitification_users = functions.firestore.document("appointment/{docId}").onCreate(async (snap, context) =>{

    let snapData = snap.data();
    
    let token = snapData.token;



    var registrationTokens = [];
    registrationTokens.push(token)
    
    
    let response = await admin.messaging().sendMulticast(
                        {
                            tokens: registrationTokens,
                            notification: {
                                    title: "Reservation",
                                    body: "Your appointment has been confirmed!"
                                }
                            }
                        );
    
                    
    

});

