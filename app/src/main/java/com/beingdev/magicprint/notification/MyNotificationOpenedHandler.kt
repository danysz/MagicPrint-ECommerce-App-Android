package com.beingdev.magicprint.notification

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.beingdev.magicprint.MainActivity
import com.beingdev.magicprint.NotificationActivity
import com.beingdev.magicprint.init.CustomApplication
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal.NotificationOpenedHandler

/**
 * Created by kshitij on 6/1/18.
 */
class MyNotificationOpenedHandler : NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val activityToBeOpened: String?
        //While sending a Push notification from OneSignal dashboard
// you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
//If key is "activityToBeOpened" and value is "AnotherActivity", then when a user clicks
//on the notification, AnotherActivity will be opened.
//Else, if we have not set any additional data MainActivity is opened.
        if (data != null) {
            activityToBeOpened = data.optString("activityToBeOpened", null)
            if (activityToBeOpened != null && activityToBeOpened == "NotificationActivity") {
//                Log.i("OneSignalExample", "customkey set with value: $activityToBeOpened")
                val intent = Intent(CustomApplication.context, NotificationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                CustomApplication.context?.startActivity(intent)
            } else if (activityToBeOpened != null && activityToBeOpened == "MainActivity") {
//                Log.i("OneSignalExample", "customkey set with value: $activityToBeOpened")
                val intent = Intent(CustomApplication.context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                CustomApplication.context?.startActivity(intent)
            } else {
                val intent = Intent(CustomApplication.context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                CustomApplication.context?.startActivity(intent)
            }
        }
        //If we send notification with action buttons we need to specify the button id's and retrieve it to
//do the necessary operation.
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID)
            if (result.action.actionID == "ActionOne") {
                Toast.makeText(CustomApplication.context, "ActionOne Button was pressed", Toast.LENGTH_LONG).show()
            } else if (result.action.actionID == "ActionTwo") {
                Toast.makeText(CustomApplication.context, "ActionTwo Button was pressed", Toast.LENGTH_LONG).show()
            }
        }
    }
}