package com.example.zhouzhihui.audioshift

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import com.example.zhouzhihui.audioshift.record.Recorder
import com.example.zhouzhihui.audioshift.ui.*
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import com.example.zhouzhihui.audioshift.util.isCancelled
import com.zzh.cooldialog.COOL_STYLE_ROTATE
import com.zzh.cooldialog.CoolDialog
import com.zzh.cooldialog.CoolStyle
import com.zzh.ui.cooledittext.CoolEditText
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import javax.inject.Inject

val PERMISSIONS = arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
val PERMISSIONS_CODE = 1
class MainActivity : BaseActivity() {
    var mAboutDialog: CoolDialog? = null
    var mCountDownTimer: CountDownTimer? = null
    var mSaveRecordFileDialog: CoolDialog? = null
    private fun hasRequiredPermissions(): Boolean = PERMISSIONS.none { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
    fun requestRequiredPermissions(view: View?) = ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_CODE)
    override fun onCreate(savedInstanceState: Bundle?) {
        val iconWidth = resources.getDimensionPixelOffset(R.dimen.dialog_icon_width)
        val iconLeftMargin = ScreenUtil.dp2px(this, 7f)
        val logo = resources.getDrawable(R.drawable.icon)
        logo.setBounds(0, 0, iconWidth, iconWidth)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AudioApp.getComponent(this)?.inject(this)
        (toolbar as? Toolbar)?.navigationIcon = logo
        setSupportActionBar(toolbar as? Toolbar)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
//        getSupportActionBar()?.setLogo(logo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        setAudioTakeButton()
        if (!hasRequiredPermissions()) {
            requestRequiredPermissions(null)
        }
        var right = 0
        (toolbar as? Toolbar)?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener  {
            override fun onPreDraw(): Boolean {
                for (i in 0 until ((toolbar as? Toolbar)?.childCount ?: -1)) {
                    val child = (toolbar as? Toolbar)?.getChildAt(i)
                    if ((child as? ImageView)?.drawable === logo) {
                        (child as ImageView).adjustViewBounds = true
                        val lp = child.layoutParams
                        lp.width = iconWidth
                        lp.height = iconWidth
                        child.x = iconLeftMargin.toFloat()
                        right = child.right
                        Log.i(TAG, "imageview right : ${child.right}")
                    } else if (child is TextView) {
                        Log.i(TAG, "textview x : ${iconLeftMargin.toFloat() * 2 + iconWidth}")
                        child.x = iconLeftMargin.toFloat() * 3 + iconWidth * 2
                    }
                }
                (toolbar as? Toolbar)?.viewTreeObserver?.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (hasRequiredPermissions()) { required_permission_layout?.visibility = View.GONE }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!hasRequiredPermissions()) {
            with(Intent()) {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + this@MainActivity.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                this@MainActivity.startActivity(this)
            }
        }
        required_permission_layout?.visibility = if (!hasRequiredPermissions()) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_about -> {
                showAboutDialog()
//                var author = SpannableString(resources.getString(R.string.about_app_author))
//                Linkify.addLinks(author, Linkify.WEB_URLS)
//
//                var email = SpannableString(resources.getString(R.string.about_app_email))
//                Linkify.addLinks(email, Linkify.WEB_URLS)
//
//                var icons_by = SpannableString(resources.getString(R.string.about_icon_by))
//                Linkify.addLinks(icons_by, Linkify.WEB_URLS)
//
//                var icon_from = SpannableString(resources.getString(R.string.about_icon_from))
//                Linkify.addLinks(icon_from, Linkify.WEB_URLS)
//
//                var msg = SpannableStringBuilder()
//                msg.append(author).appendln().append(email).appendln().append(icons_by).appendln().append(icon_from)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() = run {
        if (mAboutDialog == null) {
            val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
//                val myMessage = Html.fromHtml(resources.getString(R.string.about_msg))
            var extraStr = TextView(this)
            var padding = ScreenUtil.dp2px(this, 20f)
            extraStr.setText(R.string.about_app_public_wx)
            extraStr.setTextColor(Color.WHITE)
            extraStr.setPadding(0, padding, 0, padding / 3)
            var extraImg = ImageView(this)
            extraImg.setImageResource(R.mipmap.wx_public)
            var lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER

            mAboutDialog = CoolDialog(this)
            mAboutDialog?.withIcon(R.drawable.icon)
                    ?.withTitle(resources.getString(R.string.about_app))
                    ?.withMsg(myMessage)
                    ?.withContentCustom(null, R.layout.layout_dialog_content_custom)
                    ?.withContentCustom(extraStr)
                    ?.withContentCustom(extraImg)
                    ?.withPositiveBtn(resources.getString(android.R.string.yes))
                    ?.withDuration(500)
                    ?.withCoolStyle(CoolStyle(mAboutDialog?.mRootView))
        }
        if (!isDestroyed && !isFinishing) {
            mAboutDialog?.show()
        }
    }

    private fun showSaveRecordFileDialog() = run {
        if (mSaveRecordFileDialog == null) {
            mSaveRecordFileDialog = CoolDialog(this)
            mSaveRecordFileDialog?.withIcon(R.drawable.icon)
                    ?.withTitle(resources.getString(R.string.save_record_file_title))
                    ?.withMsg(resources.getString(R.string.save_record_file_msg))
                    ?.withNegativeBtn(resources.getString(android.R.string.no))
                    ?.withPositiveBtn(resources.getString(R.string.save), mSaveRecordFileDialog?.btnPositiveBg, object : CoolDialog.CoolDialogClickListener(mSaveRecordFileDialog) {
                        override fun onClick(v: View?) {
                            super.onClick(v)
                            saveRecordFile(recorder?.getRecordFile(), mSaveRecordFileDialog?.mRootView?.findViewById<CoolEditText>(R.id.et_audio_file_name)?.text?.toString())
                            tv_match_voice_count.text = getRecordedFileNum(recorder?.getRecordFile())
                        }
                    })
                    ?.withDuration(300)
                    ?.withDismissListener(object : CoolDialog.CoolDialogDismissListener(mSaveRecordFileDialog) {
                        override fun onDismiss(dialog: DialogInterface?) {
                            super.onDismiss(dialog)
                            onDialogDismiss()
                        }
                    })
                    ?.withCancelable(false)
                    ?.withCanceledOnTouchOutside(false)
                    ?.withContentCustom(null, R.layout.layout_save_file_et)
                    ?.withCoolStyle(CoolStyle(mSaveRecordFileDialog?.mRootView, COOL_STYLE_ROTATE))
            val edit = mSaveRecordFileDialog?.mRootView?.findViewById<CoolEditText>(R.id.et_audio_file_name)
            GradientDrawable().apply {
                cornerRadius = resources.getDimension(R.dimen.btn_common_radius)
                setColor(Color.WHITE)
                edit?.setBackgroundDrawable(this)
                ((edit?.parent as? View)?.layoutParams as? LinearLayout.LayoutParams)?.setMargins(0, ScreenUtil.dp2px(this@MainActivity, 20f), 0, 0)
            }
        }
        if (!isDestroyed && !isFinishing) {
            if (recorder?.getRecordFile()?.length() ?: 0 > 0) {
                mSaveRecordFileDialog?.mRootView?.findViewById<CoolEditText>(R.id.et_audio_file_name)?.setText(getSaveFileName(recorder?.getRecordFile()))
                mSaveRecordFileDialog?.show()
            } else {
                Toast.makeText(this@MainActivity, "录音失败", Toast.LENGTH_SHORT).show()
                onDialogDismiss()
            }
        }
    }

    fun onDialogDismiss() {
        probar_voice_timer?.max = durationInMillis.toInt()
        probar_voice_timer?.progress = 0
        tv_voice_timer?.text = "00:00:000"
    }

    private fun setAudioTakeButton() {
        tv_voice_timer?.text = "00:00:000"
        tv_match_voice_count.text = getRecordedFileNum(recorder?.getRecordFile())
        autio_take_circle.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                bigger(v, 50)
            } else if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
                smaller(v, 50)
                if (isCancelled(v, event)) {
                } else {
                    (v.parent as? View)?.performClick()
                }
            } else if (event.action == MotionEvent.ACTION_MOVE) {
            }
            true
        })
        audio_take_container.setOnClickListener {
            if (isRecording()) {
                stopRecording()
            } else {
                startRecording()
            }
        }
    }

    fun audioAnim(startAnim: Boolean) {
        startVoiceStateAnimation(state_animation, startAnim)
        startBtnPlayAnimation(audio_take, startAnim)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("aboutDialog", mAboutDialog?.isShowing ?: false)
        // todo save state
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        takeIf { savedInstanceState?.getBoolean("aboutDialog") ?: false }?.run { showAboutDialog() }
    }

    private var startTime: Long = -1
    var recorder: Recorder? = null @Inject set
    var durationInMillis: Long = 0L @Inject set
    private fun isRecording(): Boolean = recorder?.isRecording() ?: false
    private fun hasRecording(): Boolean = !isRecording() && recorder?.hasRecording() ?: false

    fun startRecording() =
//        elf.setEnabled(false)
//        santa.setEnabled(false)
            takeIf { mAboutDialog?.isShowing != true && mSaveRecordFileDialog?.isShowing != true }?.run {
                recorder?.startRecording()
                startTime = System.currentTimeMillis()
                probar_voice_timer?.max = durationInMillis.toInt()
                audioAnim(isRecording())
                mCountDownTimer?.cancel()
                mCountDownTimer = object : CountDownTimer(durationInMillis, 10) {
                    override fun onFinish() {
                        tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis)
                        stopRecording()// onFinish
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        probar_voice_timer?.progress = (System.currentTimeMillis() - startTime).toInt()
                        tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis - millisUntilFinished)
                    }
                }
                mCountDownTimer?.start()
            }


    fun stopRecording() {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
        takeIf { isRecording() }?.run {
            probar_voice_timer?.progress = (System.currentTimeMillis() - startTime).toInt()
            startTime = -1
            showSaveRecordFileDialog()
            recorder?.stopRecording()
            audioAnim(isRecording())
//        elf.setEnabled(true)
//        santa.setEnabled(true)
        }
    }
}
