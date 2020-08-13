'use strict'
const functions = require('firebase-functions');

const admin=require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotifications = functions.database.ref('/Notifications/{receiver_user_id}/{notification_id}')
.onWrite((data,context) =>
{
const receiver_user_id=context.params.receiver_user_id;
const notification_id=context.params.notification_id;
console.log('we have notification to send to:',receiver_user_id)


if(!data.after.val()){ //if notification is deleted/not there
    console.log('A notification has been deleted',notification_id)
    return null
}

const DeviceToken=admin.database().ref(`/Users/${receiver_user_id}/device_token`).once('value');
return DeviceToken.then(result =>{
    const token_id=result.val();
    const payload={
        notification:{
            title:"new chat request",
            body:`you have a new chat request`,
            icon:"default"

        }
    }
    return admin.messaging().sendToDevice(token_id,payload)
    .then(response=>{
        console.log("this was a notification")
    })
})
});
