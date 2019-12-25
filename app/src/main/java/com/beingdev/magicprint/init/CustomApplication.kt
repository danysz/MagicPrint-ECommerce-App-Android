package com.beingdev.magicprint.init

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.multidex.MultiDexApplication
import com.activeandroid.ActiveAndroid
import com.beingdev.magicprint.R
import com.beingdev.magicprint.notification.MyNotificationOpenedHandler
import com.beingdev.magicprint.notification.MyNotificationReceivedHandler
import com.bumptech.glide.Glide
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.backgroundColorRes
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader.Companion.init
import com.mikepenz.materialdrawer.util.DrawerImageLoader.Tags
import com.mikepenz.materialdrawer.util.DrawerUIUtils.getPlaceHolder
import com.onesignal.OneSignal


/**
 * Created by mikepenz on 27.03.15.
 */
class CustomApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        //Database ORMhelper class
        ActiveAndroid.initialize(this)
        context = applicationContext
        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
//MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(MyNotificationReceivedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        //initialize and create the image loader logic
        init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                Glide.with(imageView.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Glide.with(imageView.context).clear(imageView)
            }

            override fun placeholder(ctx: Context, tag: String?): Drawable { //define different placeholders for different imageView targets
//default tags are accessible via the DrawerImageLoader.Tags
//custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (Tags.PROFILE.name == tag) {
                    return getPlaceHolder(ctx)
                } else if (Tags.ACCOUNT_HEADER.name == tag) {
                    return IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56)
                } else if ("customUrlItem" == tag) {
                    return IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56)
                }
                //we use the default one for
//DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                return super.placeholder(ctx, tag)
            }
        })
    }


    companion object {
        var context: Context? = null
            private set
    }
}