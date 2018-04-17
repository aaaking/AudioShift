package com.example.zhouzhihui.audioshift.sgs.layaair.autoupdateversion;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogHelper {

    public static void Confirm(Context ctx, CharSequence title, CharSequence message,  
            CharSequence okText, OnClickListener oklistener, CharSequence cancelText,  
            OnClickListener cancellistener) {  
        Builder builder = createDialog(ctx, title, message);
        builder.setPositiveButton(okText, oklistener);
        builder.setNegativeButton(cancelText, cancellistener);
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);	//防止点到外面自动关掉对话框。
        dlg.show();
    }

    private static Builder createDialog(Context ctx, CharSequence title,
            CharSequence message) {
        Builder builder = new Builder(ctx);
        builder.setMessage(message);
        if(title!=null)
        {
            builder.setTitle(title);
        }
        return builder;
    }

    @SuppressWarnings("unused")
    private static Builder createDialog(Context ctx,int titleId, int messageId) {
        Builder builder = new Builder(ctx);
        builder.setMessage(messageId);  
        builder.setTitle(titleId);  
        return builder;  
    }
  
}  