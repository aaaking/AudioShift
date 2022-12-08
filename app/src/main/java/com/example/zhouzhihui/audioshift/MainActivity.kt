package com.example.zhouzhihui.audioshift

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import com.example.zhouzhihui.audioshift.play.Player
import com.example.zhouzhihui.audioshift.record.Recorder
import com.example.zhouzhihui.audioshift.sgs.startSgsAC
import com.example.zhouzhihui.audioshift.ui.*
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import com.example.zhouzhihui.audioshift.util.isCancelled
import com.zzh.cooldialog.COOL_STYLE_ROTATE
import com.zzh.cooldialog.CoolDialog
import com.zzh.cooldialog.CoolStyle
import com.zzh.ui.cooledittext.CoolEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_right.*
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject


val PERMISSIONS = arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
val PERMISSIONS_CODE = 1
val REQUEST_CODE_SELECT_AUDIO_FILE = 6
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, IClick {
    var mAboutDialog: CoolDialog? = null
    var mCountDownTimer: CountDownTimer? = null
    var mSaveRecordFileDialog: CoolDialog? = null
    var recordedFilesBeforeShowRightDrawer: ArrayList<File> = ArrayList<File>()
    private fun hasRequiredPermissions(): Boolean = PERMISSIONS.none { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
    fun requestRequiredPermissions(view: View?) = ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_CODE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_stop.setOnClickListener {
            stopRecording() // 停止
        }
        AudioApp.getComponent(this)?.inject(this)
        setAudioTakeButton()
        if (!hasRequiredPermissions()) {
            requestRequiredPermissions(null)
        }
        (toolbar as? Toolbar)?.apply { setToolbar(this,  R.drawable.icon, drawer_layout) }
        nav_view.setNavigationItemSelectedListener(this)
//        drawer_layout.setScrimColor(Color.TRANSPARENT)//Removing the Scrim then the main content will not darken
//        drawer_layout.drawerElevation = 0f//Sometimes the drawer comes with a shadow, which can be removed by adding the following line.
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
//            private val scaleFactor = 6f
            override fun onDrawerStateChanged(newState: Int) {
            }
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
//                content.translationX = (drawerView?.width ?: 0) * slideOffset
//                content.scaleX = 1 - (slideOffset / scaleFactor)
//                content.scaleY = 1 - (slideOffset / scaleFactor)
//                Log.i("zzh", "${drawer_layout.isDrawerVisible(GravityCompat.START)}")
            }
            override fun onDrawerClosed(drawerView: View?) {
            }
            override fun onDrawerOpened(drawerView: View?) {
                if (drawer_layout?.isDrawerOpen(GravityCompat.END) == true) {
                    if (right_drawer_recyclerview == null) {
                        viewstub_right_drawlayout?.inflate()
                        right_drawer_recyclerview?.adapter = RightDrawerAdap(recorder?.getRecordFile()?.parentFile?.listFiles()?.filter { it.absolutePath != recorder?.getRecordFile()?.absolutePath }, this@MainActivity)//init recyclerview only once, if adapter != null do nothing
                        open_file_directory?.setOnClickListener {
                            Intent(Intent.ACTION_GET_CONTENT).apply {
                                setDataAndType(Uri.parse(recorder?.getRecordFile()?.parentFile?.absolutePath), "file/*")
                                if (resolveActivityInfo(packageManager, 0) != null) {
                                    startActivityForResult(this, REQUEST_CODE_SELECT_AUDIO_FILE)
                                }
                            }
                        }
                        refresh_file_directory?.setOnClickListener {
                            (right_drawer_recyclerview?.adapter as? RightDrawerAdap)?.updateData(recorder?.getRecordFile()?.parentFile?.listFiles()?.filter { it.absolutePath != recorder?.getRecordFile()?.absolutePath })
                            tv_match_voice_count?.text = getLatestModifiedFileName(recorder?.getRecordFile())
                            recordedFilesBeforeShowRightDrawer.clear()
                        }
                        recordedFilesBeforeShowRightDrawer.clear()
                        main_radiogroup?.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                                when (checkedId) {
                                    R.id.main_radiobutton_weixing -> {
                                        iv_player?.setImageResource(R.drawable.player_normal_anim)
                                        mPlayType = SPEED_NORMAL
                                    }
                                    R.id.main_radiobutton_contacts -> {
                                        iv_player?.setImageResource(R.drawable.player_baby_anim)
                                        mPlayType = SPEED_BABY
                                    }
                                    R.id.main_radiobutton_find -> {
                                        iv_player?.setImageResource(R.drawable.player_aged_anim)
                                        mPlayType = SPEED_AGED
                                    }
                                }
                            }
                        })
                    } else if (recordedFilesBeforeShowRightDrawer.size > 0) {
                        notifyRightDrawerListData()
                    }
                }
            }
        })
    }

    fun notifyRightDrawerListData() = (right_drawer_recyclerview?.adapter as? RightDrawerAdap)?.apply {
        mDatas.addAll(recordedFilesBeforeShowRightDrawer)
        notifyItemRangeInserted(mDatas.size, recordedFilesBeforeShowRightDrawer.size)
        recordedFilesBeforeShowRightDrawer.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "${requestCode}   ${resultCode}  ${data?.getData()}  ${data?.getData()?.path}")
        if (requestCode == REQUEST_CODE_SELECT_AUDIO_FILE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "${File(data?.getData()?.path).name} ${recorder?.getRecordFile()?.path}")
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.open_record_file -> {
                if (!drawer_layout.isDrawerOpen(GravityCompat.END)) {
                    drawer_layout.openDrawer(GravityCompat.END)
                }
            }
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

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            drawer_layout.isDrawerOpen(GravityCompat.END) -> drawer_layout.closeDrawer(GravityCompat.END)
            else -> super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_sgs -> {
                startSgsAC(this)
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showAboutDialog() = run {
        if (mAboutDialog == null) {
            val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
//                val myMessage = Html.fromHtml(resources.getString(R.string.about_msg))
            var extraStr = TextView(this)
            var padding = ScreenUtil.dp2px(this, 20f)
            extraStr.setText(R.string.about_app_public_wx)
            extraStr.setTextColor(Color.WHITE)
            extraStr.setPadding(0, 0, 0, padding / 3)
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
                    ?.withDuration(300)
                    ?.withCoolStyle(CoolStyle(mAboutDialog?.mRootView))
        }
        if (!isACDestroyed() && !isFinishing) {
            mAboutDialog?.show()
        }
    }

    private fun showSaveRecordFileDialog() = run {
        if (mSaveRecordFileDialog == null) {
            mSaveRecordFileDialog = CoolDialog(this)
            mSaveRecordFileDialog?.withIcon(R.drawable.icon)
                    ?.withTitle(resources.getString(R.string.save_record_file_title))
                    ?.withMsg(resources.getString(R.string.save_record_file_msg))
                    ?.withNegativeBtn(resources.getString(android.R.string.cancel), mSaveRecordFileDialog?.btnNegativeBg, object : CoolDialog.CoolDialogClickListener(mSaveRecordFileDialog) {
                        override fun onClick(v: View?) {
                            recorder?.getRecordFile()?.delete()
                            super.onClick(v)
                        }
                    })
                    ?.withPositiveBtn(resources.getString(R.string.save), mSaveRecordFileDialog?.btnPositiveBg, object : CoolDialog.CoolDialogClickListener(mSaveRecordFileDialog) {
                        override fun onClick(v: View?) {
                            super.onClick(v)
                            val fileName = mSaveRecordFileDialog?.mRootView?.findViewById<CoolEditText>(R.id.et_audio_file_name)?.text?.toString() ?: ""
                            afterSaveRecordFile(fileName, saveRecordFile(recorder?.getRecordFile(), fileName))
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
        if (!isACDestroyed() && !isFinishing) {
            if (recorder?.getRecordFile()?.length() ?: 0 > 0) {
                mSaveRecordFileDialog?.mRootView?.findViewById<CoolEditText>(R.id.et_audio_file_name)?.setText(getSaveFileName(recorder?.getRecordFile()))
                mSaveRecordFileDialog?.show()
            } else {
                Toast.makeText(this@MainActivity, "录音失败", Toast.LENGTH_SHORT).show()
                onDialogDismiss()
            }
        }
    }

    fun afterSaveRecordFile(fileName: String?, saveSuccess: Boolean) = fileName?.takeIf { saveSuccess && fileName.trim().isNotEmpty() }?.run {
        tv_match_voice_count?.text = fileName
        recorder?.getRecordFile()?.parentFile?.absolutePath?.apply {
            recordedFilesBeforeShowRightDrawer.add(File(this, fileName))
        }
        if (drawer_layout?.isDrawerOpen(GravityCompat.END) == true && recordedFilesBeforeShowRightDrawer.size > 0) {
            notifyRightDrawerListData()
        }
    }

    fun onDialogDismiss() {
        probar_voice_timer?.max = durationInMillis.toInt()
        probar_voice_timer?.progress = 0
        tv_voice_timer?.text = "00:00:000"
    }

    private fun setAudioTakeButton() {
        iv_player.setOnClickListener {
            if (isPlaying()) {
                stopPlaying()
            } else {
                startPlaying()
            }
        }
        tv_voice_timer?.text = "00:00:000"
        tv_match_voice_count?.text = getLatestModifiedFileName(recorder?.getRecordFile())
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
            if (isPausing()) {
                resumeRecording()
            } else if (isRecording()) {
                pauseRecording()
            } else {
                startRecording()
            }
        }
    }

    fun audioAnim(startAnim: Boolean) {
        startVoiceStateAnimation(state_animation, startAnim)
        startBtnPlayAnimation(audio_take, startAnim)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("aboutDialog", mAboutDialog?.isShowing ?: false)
        // todo 1 save state  2 record failed  3 night theme
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        takeIf { savedInstanceState?.getBoolean("aboutDialog") ?: false }?.run { showAboutDialog() }
    }

    private var startTime: Long = -1
    var recorder: Recorder? = null @Inject set
    var player: Player? = null @Inject set
    var durationInMillis: Long = 0L @Inject set
    var hasRecordedTime: Long = 0
    private fun isRecording(): Boolean = recorder?.isRecording() ?: false
    private fun isPausing(): Boolean = recorder?.isPausing() ?: false

    fun startRecording() =
            takeIf { mAboutDialog?.isShowing != true && mSaveRecordFileDialog?.isShowing != true }?.run {
                iv_player?.isEnabled = false
                hasRecordedTime = 0
                recorder?.startRecording()
                startTime = System.currentTimeMillis()
                probar_voice_timer?.max = durationInMillis.toInt()
                audioAnim(isRecording())
                mCountDownTimer?.cancel()
                btn_stop.visibility = View.VISIBLE
                mCountDownTimer = object : CountDownTimer(durationInMillis - hasRecordedTime, 10) {
                    override fun onFinish() {
                        tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis)
                        stopRecording() // onFinish
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        probar_voice_timer?.progress = (System.currentTimeMillis() - startTime).toInt()
                        tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis - hasRecordedTime - millisUntilFinished)
                    }
                }
                mCountDownTimer?.start()
            }

    fun pauseRecording() {
        hasRecordedTime = System.currentTimeMillis() - startTime
        mCountDownTimer?.cancel()
        recorder?.pause()
        audioAnim(false) // pause
    }

    fun resumeRecording() {
        mCountDownTimer?.cancel()
        startTime = System.currentTimeMillis()
        recorder?.resume()
        mCountDownTimer = object : CountDownTimer(durationInMillis - hasRecordedTime, 10) {
            override fun onFinish() {
                tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis)
                stopRecording() // onFinish
            }

            override fun onTick(millisUntilFinished: Long) {
                probar_voice_timer?.progress = (System.currentTimeMillis() - startTime).toInt()
                tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(durationInMillis - hasRecordedTime - millisUntilFinished)
            }
        }
        mCountDownTimer?.start()
        audioAnim(true) // resume
    }

    fun stopRecording() {
        btn_stop.visibility = View.GONE
        mCountDownTimer?.cancel()
        takeIf { isRecording() }?.run {
            startTime = -1
            showSaveRecordFileDialog()
            recorder?.stopRecording()
            audioAnim(false)
            iv_player?.isEnabled = true
        }
    }

    var mPlayType: Pair<Int, Float> = SPEED_NORMAL
    var mIsPlaying = false
    private fun isPlaying(): Boolean = mIsPlaying//player?.isPlaying() ?: false
    fun startPlaying() {
        val curFile = File(recorder?.getRecordFile()?.parentFile?.absolutePath, tv_match_voice_count?.text?.toString())
        if (curFile.exists() && curFile.isFile && curFile.length() > 0) {
            mIsPlaying = true
            updatePlayingState()
            player?.setSpeed(mPlayType.second)
            player?.startPlaying(curFile)
            val audioLengthSeconds = curFile.length() / (player?.getPlaybackrate() ?: 48000) / 2f
            val audioLengthMillis: Long = (audioLengthSeconds * 1000 + 280).toLong()
            Log.i(TAG, "long: ${curFile.length()}  ${audioLengthMillis} int: ${audioLengthSeconds} ")
            probar_voice_timer?.max = audioLengthMillis.toInt()
            mCountDownTimer?.cancel()
            mCountDownTimer = object : CountDownTimer(audioLengthMillis, 10) {
                override fun onFinish() {
                    probar_voice_timer?.progress = audioLengthMillis.toInt()
                    stopPlaying()
                }
                override fun onTick(millisUntilFinished: Long) {
                    probar_voice_timer?.progress = (audioLengthMillis - millisUntilFinished).toInt()
                    tv_voice_timer?.text = SimpleDateFormat("mm:ss:SSS").format(audioLengthMillis - millisUntilFinished)
                }
            }
            mCountDownTimer?.start()
        }
    }
    fun stopPlaying() = takeIf { isPlaying() }?.run {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
        mIsPlaying = false
        runOnUiThread { updatePlayingState() }
    }

    fun updatePlayingState() {
        (iv_player.drawable as? Animatable)?.apply { if (isRunning) stop() else start() }
        startVoiceStateAnimation(state_animation, isPlaying())
        audio_take_container?.isEnabled = !isPlaying()
        autio_take_circle?.isEnabled = !isPlaying()
    }

    fun changePlayType(playType: Pair<Int, Float>) { mPlayType = playType }

    override fun onClick(position: Int, payload: Any?) {
        var validFile = payload is File && payload.isFile && payload.length() > 0
        if (validFile) {
            tv_match_voice_count?.text = (payload as? File)?.name
            drawer_layout?.closeDrawer(GravityCompat.END)
        } else {
            (right_drawer_recyclerview?.adapter as? RightDrawerAdap)?.mDatas?.removeAt(position)
            right_drawer_recyclerview?.adapter?.notifyItemRemoved(position)
        }
        Log.i(TAG, "onClick $position $payload $validFile")
        super.onClick(position, payload)
    }
    override fun onLongPress(position: Int, payload: Any?): Boolean {
        var validFile = payload is File && payload.isFile && payload.length() > 0
        if (validFile) {

        } else {
            (right_drawer_recyclerview?.adapter as? RightDrawerAdap)?.mDatas?.removeAt(position)
            right_drawer_recyclerview?.adapter?.notifyItemRemoved(position)
        }
        Log.i(TAG, "onLongPress $position $payload $validFile")
        return super.onLongPress(position, payload)
    }
    override fun onDelete(position: Int, payload: Any?) {
        super.onDelete(position, payload)
    }
}
val SPEED_NORMAL = Pair(0, 1f)
val SPEED_BABY = Pair(1, 2f)
val SPEED_AGED = Pair(2, 0.75f)
