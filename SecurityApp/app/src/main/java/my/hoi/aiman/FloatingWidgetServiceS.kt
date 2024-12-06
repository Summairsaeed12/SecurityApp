package my.hoi.aiman

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.telephony.SmsManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import com.hoi.aiman.R


private val mWindowManager: WindowManager? = null
private val mFloatingWidgetView: View? = null
private var collapsedView: android.view.View? = null
private var expandedView: android.view.View? = null
private val remove_image_view: ImageView? = null
private val szWindow = Point()
private val removeFloatingWidgetView: View? = null

private val seekBarForHeight: SeekBar? = null

public class FloatingWidgetServiceS() : Service(),
    View.OnClickListener {
    private var mWindowManager: WindowManager? = null
    private var mFloatingWidgetView: View? = null
    private var collapsedView: View? = null
    private var expandedView: View? = null
    private var expanded: View? = null
    private var remove_image_view: ImageView? = null
    private val szWindow = Point()
    private var removeFloatingWidgetView: View? = null
    private var seekBarForHeight: SeekBar? = null
    private var x_init_cord = 0
    private var y_init_cord = 0
    private var x_init_margin = 0
    private var y_init_margin = 0

    var LinearLayoutl: LinearLayout? = null
    var btn: ImageView? = null

    //Variable to check if the Floating widget view is on left side or in right side
    // initially we are displaying Floating widget view to Left side so set it to true
    private var isLeft = true
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object CSEt
    {
        var str  =  "000000000"
    }
    override fun onCreate() {
        super.onCreate()
        //init WindowManager
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        getWindowManagerDefaultDisplay()
        //Init LayoutInflater
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //addRemoveView(inflater)
  //      addFloatingWidgetView(inflater)
//        implementClickListeners()
//        implementTouchListenerToFloatingWidgetView()

//        collapsedView!!.visibility = View.VISIBLE
//        expandedView!!.visibility = View.GONE
//        expanded!!.visibility = View.GONE
//        btn!!.visibility = View.GONE
//        LinearLayoutl!!.visibility = View.GONE



    }
    fun sendMsg()
    {
        val smsManager: SmsManager = SmsManager.getDefault()

        smsManager.sendTextMessage(str.toString(), null, "message", null, null)
    }
    /*  Add Remove View to Window Manager  */
    private fun addRemoveView(inflater: LayoutInflater): View? {
        //Inflate the removing view layout we created
        removeFloatingWidgetView =
            inflater.inflate(R.layout.remove_floating_widget_layout, null)

        //Add the view to the window.
        val paramRemove: WindowManager.LayoutParams
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            paramRemove = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            paramRemove = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        //Specify the view position
        paramRemove.gravity = Gravity.TOP or Gravity.LEFT

        //Initially the Removing widget view is not visible, so set visibility to GONE
        removeFloatingWidgetView!!.setVisibility(View.GONE)
        remove_image_view =
            removeFloatingWidgetView!!.findViewById<View>(R.id.remove_img) as ImageView
        // seekBarForHeight = remfoveFloatingWidgetView!!.findViewById(R.id.expanded_container)


        //Add the view to the window
        mWindowManager!!.addView(removeFloatingWidgetView, paramRemove)
        return remove_image_view
    }

    /*  Add Floating Widget View to Window Manager  */
    private fun addFloatingWidgetView(inflater: LayoutInflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.inflate(R.layout.floating_widget_layout, null)

        //Add the view to the window.
        val params: WindowManager.LayoutParams
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }


        //Specify the view position
        params.gravity = Gravity.TOP or Gravity.LEFT

        //Initially view will be added to top-left corner, you change x-y coordinates according to your need
        params.x = 0
        params.y = 100

        //Add the view to the window
        mWindowManager!!.addView(mFloatingWidgetView, params)

        //find id of collapsed view layout
        collapsedView = mFloatingWidgetView!!.findViewById(R.id.collapse_view)

        //find id of the expanded view layout
        expandedView = mFloatingWidgetView!!.findViewById(R.id.expanded_container)
        expanded = mFloatingWidgetView!!.findViewById(R.id.expanded)




    }

    private fun getWindowManagerDefaultDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) mWindowManager!!.defaultDisplay.getSize(
            szWindow
        ) else {
            val w = mWindowManager!!.defaultDisplay.width
            val h = mWindowManager!!.defaultDisplay.height
            szWindow[w] = h
        }
    }

    /*  Implement Touch Listener to Floating Widget Root View  */
    private fun implementTouchListenerToFloatingWidgetView() {
        //Drag and move floating view using user's touch action
        // .
        mFloatingWidgetView!!.findViewById<View>(R.id.root_container)
            .setOnTouchListener(object : View.OnTouchListener {
                var time_start: Long = 0
                var time_end: Long = 0
                var isLongClick = false //variable to judge if user click long press
                var inBounded =
                    false //variable to judge if floating view is bounded to remove view
                var remove_img_width = 0
                var remove_img_height = 0
                var handler_longClick = Handler()
                var runnable_longClick: Runnable = Runnable { //On Floating Widget Long Click

                    //Set isLongClick as true
                    isLongClick = true

                    //Set remove widget view visibility to VISIBLE
                    removeFloatingWidgetView!!.visibility = View.VISIBLE
                    onFloatingWidgetLongClick()
                }

                override fun onTouch(v: View, event: MotionEvent): Boolean {

                    //Get Floating widget view params
                    val layoutParams =
                        mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams

                    //get the touch location coordinates
                    val x_cord = event.rawX.toInt()
                    val y_cord = event.rawY.toInt()
                    val x_cord_Destination: Int
                    var y_cord_Destination: Int
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            time_start = System.currentTimeMillis()
                            handler_longClick.postDelayed(runnable_longClick, 600)
                            remove_img_width = remove_image_view!!.layoutParams.width
                            remove_img_height = remove_image_view!!.layoutParams.height
                            x_init_cord = x_cord
                            y_init_cord = y_cord

                            //remember the initial position.
                            x_init_margin = layoutParams.x
                            y_init_margin = layoutParams.y
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            isLongClick = false
                            removeFloatingWidgetView!!.visibility = View.GONE
                            remove_image_view!!.layoutParams.height = remove_img_height
                            remove_image_view!!.layoutParams.width = remove_img_width
                            handler_longClick.removeCallbacks(runnable_longClick)

                            //If user drag and drop the floating widget view into remove view then stop the service
                            if (inBounded) {
                                stopSelf()
                                var sharedPreferences = applicationContext.getSharedPreferences(
                                    "Filesave",
                                    Context.MODE_PRIVATE
                                )
                                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                                editor.putString("active", "false")
                                editor.apply()
                                editor.commit()
                                editor.apply()
                                inBounded = false
                                return false
                            }


                            //Get the difference between initial coordinate and current coordinate
                            val x_diff = x_cord - x_init_cord
                            val y_diff = y_cord - y_init_cord

                            //The check for x_diff <5 && y_diff< 5 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                                time_end = System.currentTimeMillis()

                                //Also check the difference between start time and end time should be less than 300ms
                                if (time_end - time_start < 300) onFloatingWidgetClick()
                            }
                            y_cord_Destination = y_init_margin + y_diff
                            val barHeight = getStatusBarHeight()
                            if (y_cord_Destination < 0) {
                                y_cord_Destination = 0
                            } else if (y_cord_Destination + (mFloatingWidgetView!!.height + barHeight) > szWindow.y) {
                                y_cord_Destination =
                                    szWindow.y - (mFloatingWidgetView!!.height + barHeight)
                            }
                            layoutParams.y = y_cord_Destination
                            inBounded = false

                            //reset position if user drags the floating view
                            resetPosition(x_cord)
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val x_diff_move = x_cord - x_init_cord
                            val y_diff_move = y_cord - y_init_cord
                            x_cord_Destination = x_init_margin + x_diff_move
                            y_cord_Destination = y_init_margin + y_diff_move

                            //If user long click the floating view, update remove view
                            if (isLongClick) {
                                val x_bound_left =
                                    szWindow.x / 2 - (remove_img_width * 1.5).toInt()
                                val x_bound_right =
                                    szWindow.x / 2 + (remove_img_width * 1.5).toInt()
                                val y_bound_top = szWindow.y - (remove_img_height * 1.5).toInt()

                                //If Floating view comes under Remove View update Window Manager
                                if (x_cord >= x_bound_left && x_cord <= x_bound_right && y_cord >= y_bound_top) {
                                    inBounded = true
                                    val x_cord_remove =
                                        ((szWindow.x - remove_img_height * 1.5) / 2).toInt()
                                    val y_cord_remove =
                                        (szWindow.y - (remove_img_width * 1.5 + getStatusBarHeight())).toInt()
                                    if (remove_image_view!!.layoutParams.height == remove_img_height) {
                                        remove_image_view!!.layoutParams.height =
                                            (remove_img_height * 1.5).toInt()
                                        remove_image_view!!.layoutParams.width =
                                            (remove_img_width * 1.5).toInt()
                                        val param_remove =
                                            removeFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
                                        param_remove.x = x_cord_remove
                                        param_remove.y = y_cord_remove
                                        mWindowManager!!.updateViewLayout(
                                            removeFloatingWidgetView,
                                            param_remove
                                        )
                                    }
                                    layoutParams.x = x_cord_remove + Math.abs(
                                        removeFloatingWidgetView!!.width - mFloatingWidgetView!!.width
                                    ) / 2
                                    layoutParams.y = y_cord_remove + Math.abs(
                                        removeFloatingWidgetView!!.height - mFloatingWidgetView!!.height
                                    ) / 2

                                    //Update the layout with new X & Y coordinate
                                    mWindowManager!!.updateViewLayout(
                                        mFloatingWidgetView,
                                        layoutParams
                                    )

                                } else {
                                    //If Floating window gets out of the Remove view update Remove view again
                                    inBounded = false
                                    remove_image_view!!.layoutParams.height = remove_img_height
                                    remove_image_view!!.layoutParams.width = remove_img_width
                                    onFloatingWidgetClick()
                                }
                            }
                            layoutParams.x = x_cord_Destination
                            layoutParams.y = y_cord_Destination
                            //Update the layout with new X & Y coordinate
                            mWindowManager!!.updateViewLayout(mFloatingWidgetView, layoutParams)
                            return true
                        }
                    }
                    return false
                }
            })
    }

    private fun implementClickListeners() {
        mFloatingWidgetView!!.findViewById<View>(R.id.close_floating_view)
            .setOnClickListener(this)
        mFloatingWidgetView!!.findViewById<View>(R.id.close_expanded_view)
            .setOnClickListener(this)
        mFloatingWidgetView!!.findViewById<View>(R.id.open_activity_button)
            .setOnClickListener(this)
        val linearLayout =
            mFloatingWidgetView!!.findViewById<LinearLayout>(R.id.expanded)
        val seekBar =
            mFloatingWidgetView!!.findViewById<SeekBar>(R.id.floating_widget_title_label)
        val colorSeekBar =
            mFloatingWidgetView!!.findViewById<SeekBar>(R.id.floating_widget_detail_label)
        seekBar.progress = 0
        val prevProgress = intArrayOf(0)
        val prevProgress1 = intArrayOf(0)


         LinearLayoutl  = mFloatingWidgetView!!.findViewById(R.id.background);
        var isvisible = true
        var MainLAyout : RelativeLayout = mFloatingWidgetView!!.findViewById(R.id.root_container);
         btn  = mFloatingWidgetView!!.findViewById(R.id.visibilitybtn);
        btn!!.setOnClickListener {
            if(isvisible){
                MainLAyout.visibility = View.VISIBLE
                LinearLayoutl!!.setBackgroundColor(Color.TRANSPARENT)
                btn!!.setImageResource(R.drawable.arrow)
                isvisible = false
                LinearLayoutl!!.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT



            }else{
                LinearLayoutl!!.setBackgroundColor(Color.BLACK)
                MainLAyout.visibility = View.GONE
                LinearLayoutl!!.layoutParams.height = 400

                btn!!.setImageResource(R.drawable.arrow)
                isvisible = true

            }
        }



        // colorSeekBar.thumbDrawer = DefaultThumbDrawer(15, Color.BLACK, 3, Color.WHITE)
        colorSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                expanded!!.visibility = View.VISIBLE

                val diff = progress - prevProgress1[0]

                Log.d("Progress Value " , progress.toString())
//                if (diff > 0) {

//
//                    if(diff>2){
//                        expanded!!.setBackgroundColor((Color.parseColor("#99000000")));
//
//                    }
//                    if(diff>3){
//                        expanded!!.setBackgroundColor((Color.parseColor("#Cc000000")));
//
//                    }
//                    //Toast.makeText(applicationContext, "increase "+diff, Toast.LENGTH_SHORT).show()
//
//
//
//                } else {
//
//
//                    if(diff<4){
//
//                        expanded!!.setBackgroundColor((Color.parseColor("#DD000000")));
//                    }
//                    //decrease
//                    // Toast.makeText(applicationContext, "decrease "+diff, Toast.LENGTH_SHORT).show()
//
//
//                }
                if(progress>=0 && progress <10)
                {
                        expanded!!.setBackgroundColor((Color.parseColor("#1A000000")));

                }
                if(progress>=10 && progress <20)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#40000000")));

                }

                if(progress>=20 && progress <30)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#A6000000")));

                }
                if(progress>=30 && progress <40)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#BF000000")));

                }

                if(progress>=40 && progress <50)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#C9000000")));

                }

                if(progress>=50 && progress <60)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#D4000000")));

                }

                if(progress>=70 && progress <80)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#D6000000")));

                }

                if(progress>=80 && progress <90)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#D9000000")));

                }
                if(progress>=90 && progress <=100)
                {
                    expanded!!.setBackgroundColor((Color.parseColor("#E6000000")));

                }




                prevProgress1[0] = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })







        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                var v = Resources.getSystem().getDisplayMetrics().heightPixels;
                var dp = v / (getResources()
                    .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)
                var r: Resources = resources
                val diff = progress - prevProgress[0]


                if (progress <= 5) {
                    expanded!!.visibility = View.GONE
                } else {
                    expanded!!.visibility = View.VISIBLE

                }

                if (diff > 0) {


                    expanded!!.minimumHeight = linearLayout.height + progress + 5


                } else {
                    //decrease
                    expanded!!.minimumHeight = linearLayout.height - (progress + 5)
                }
                prevProgress[0] = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.close_floating_view ->                 //close the service and remove the from from the window
            {
                stopSelf()
                var sharedPreferences = this.getSharedPreferences("Filesave", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString("active", "false")
                editor.apply()
                editor.commit()
                editor.apply()

                LinearLayoutl!!.visibility = View.GONE
                btn!!.visibility = View.GONE
            }
            R.id.close_expanded_view -> {
                collapsedView!!.visibility = View.VISIBLE
                expandedView!!.visibility = View.GONE
                expanded!!.visibility = View.GONE
                btn!!.visibility = View.GONE
                LinearLayoutl!!.visibility = View.GONE

            }
            R.id.open_activity_button -> {
                //open the activity and stop service
                val intent = Intent(this@FloatingWidgetServiceS, MainActivityOrg::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                //close the service and remove view from the view hierarchy
                stopSelf()
            }
        }
    }
    /*  on Floating Widget Long Click, increase the size of remove view as it look like taking focus */
    private fun onFloatingWidgetLongClick() {
        //Get remove Floating view params
        val removeParams = removeFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
        //get x and y coordinates of remove view
        val x_cord = (szWindow.x - removeFloatingWidgetView!!.width) / 2
        val y_cord = szWindow.y - (removeFloatingWidgetView!!.height + getStatusBarHeight())
        removeParams.x = x_cord
        removeParams.y = y_cord
        //Update Remove view params
        mWindowManager!!.updateViewLayout(removeFloatingWidgetView, removeParams)
    }

    /*  Reset position of Floating Widget view on dragging  */
    private fun resetPosition(x_cord_now: Int) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true
            moveToLeft(x_cord_now)
        } else {
            isLeft = false
            moveToRight(x_cord_now)
        }
    }

    /*  Method to move the Floating widget view to Left  */
    private fun moveToLeft(current_x_cord: Int) {
        val x = szWindow.x - current_x_cord
        object : CountDownTimer(500, 5) {
            //get params of Floating Widget view
            var mParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
            override fun onTick(t: Long) {
                val step = (500 - t) / 5
                mParams.x = 0 - (current_x_cord * current_x_cord * step).toInt()

                //If you want bounce effect uncomment below line and comment above line
                // mParams.x = 0 - (int) (double) bounceValue(step, x);


                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }

            override fun onFinish() {
                mParams.x = 0

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }
        }.start()
    }

    /*  Method to move the Floating widget view to Right  */
    private fun moveToRight(current_x_cord: Int) {
        object : CountDownTimer(500, 5) {
            //get params of Floating Widget view
            var mParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
            override fun onTick(t: Long) {
                val step = (500 - t) / 5
                mParams.x =
                    (szWindow.x + (current_x_cord * current_x_cord * step) - mFloatingWidgetView!!.width).toInt()

                //If you want bounce effect uncomment below line and comment above line
                //  mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - mFloatingWidgetView.getWidth();

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }

            override fun onFinish() {
                mParams.x = szWindow.x - mFloatingWidgetView!!.width

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }
        }.start()
    }

    /*  Get Bounce value if you want to make bounce effect to your Floating Widget */
    private fun bounceValue(step: Long, scale: Long): Double {
        return scale * Math.exp(-0.055 * step) * Math.cos(0.08 * step)
    }

    /*  Detect if the floating view is collapsed or expanded */
    private fun isViewCollapsed(): Boolean {
        return mFloatingWidgetView == null || mFloatingWidgetView!!.findViewById<View>(R.id.collapse_view).visibility == View.VISIBLE
    }

    /*  return status bar height on basis of device display metrics  */
    private fun getStatusBarHeight(): Int {
        return Math.ceil((25 * applicationContext.resources.displayMetrics.density).toDouble())
            .toInt()
    }

    /*  Update Floating Widget view coordinates on Configuration change  */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getWindowManagerDefaultDisplay()
        val layoutParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (layoutParams.y + (mFloatingWidgetView!!.height + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y =
                    szWindow.y - (mFloatingWidgetView!!.height + getStatusBarHeight())
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, layoutParams)
            }
            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x)
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x)
            }
        }
    }

    /*  on Floating widget click show expanded view  */
    private fun onFloatingWidgetClick() {
        if (isViewCollapsed()) {
            //When user clicks on the image view of the collapsed layout,
            //visibility of the collapsed layout will be changed to "View.GONE"
            //and expanded view will become visible.
            collapsedView!!.visibility = View.GONE
            expandedView!!.visibility = View.VISIBLE
            expanded!!.visibility = View.VISIBLE

            btn!!.visibility= View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        /*  on destroy remove both view from window manager */if (mFloatingWidgetView != null) mWindowManager!!.removeView(
            mFloatingWidgetView
        )
        if (removeFloatingWidgetView != null) mWindowManager!!.removeView(
            removeFloatingWidgetView
        )
    }
}