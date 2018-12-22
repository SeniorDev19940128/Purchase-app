package com.app.purchase.Views.Tabs.Debit


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG

import android.database.Cursor;

import android.provider.ContactsContract;
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.purchase.Interfaces.ClickListners.DebitCreditSaveClick

import com.app.purchase.R
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.transform.Result


/**
 * A simple [Fragment] subclass.
 */
class DebitDetailFragment(debitCreditSaveClick: DebitCreditSaveClick) : Fragment(), View.OnClickListener {


    private var creditButton: Button? = null
    private var debitButton: Button? = null
    private var debitCreditSaveButton: Button? = null
    private var roomView: View? = null
    private var creditDebitName: AutoCompleteTextView? = null
    private var creditDebitPrice: EditText? = null
    private var creditDebitNotes: EditText? = null
    private var creditOrDebit = ""
    private var debitCreditSaveClick: DebitCreditSaveClick? = debitCreditSaveClick
    private var debitEntities: CreditDebitEntities? = null
    private var ID = 0
    private var DateString = ""
    private var imageDir = ""
    private var backImage: ImageView? = null
    private var imageView: CircleImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        roomView = inflater.inflate(R.layout.fragment_debit_detail, container, false)
        initViews(roomView)
        if (arguments != null) {
            debitEntities = arguments?.getParcelable("debitEntities")
            if (debitEntities != null) {
                creditDebitName?.setText(debitEntities?.CreditDebitName)
                creditDebitPrice?.setText("".plus(debitEntities?.CreditDebitPricePrice))
                creditDebitNotes?.setText(debitEntities?.CreditDebitNotes)
                ID = debitEntities?.CreditDebitId!!
                DateString = debitEntities?.CreditDebitDate!!
                imageDir = debitEntities?.imageDir!!
                creditOrDebit = if (debitEntities?.CreditOrDebit == "Debit") {
                    setButtonBackground(R.drawable.only_border, R.drawable.round_button)
                    "Debit"
                } else {
                    setButtonBackground(R.drawable.round_button, R.drawable.only_border)
                    "Credit"
                }
            }
        } else {
            setButtonBackground(R.drawable.only_border, R.drawable.round_button)
            creditOrDebit = "Debit"
        }

        val imgFile = File(imageDir)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView?.setImageBitmap(myBitmap)
        }
        return roomView
    }

    private// Get all Contacts
    // Add Contact's Name into the List
    val allContactNames: List<String>
        get() {
            val lContactNamesList = ArrayList<String>()
            try {
                val lPeople = activity!!.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
                if (lPeople != null) {
                    while (lPeople.moveToNext()) {
                        lContactNamesList.add(lPeople.getString(lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                    }
                }
            } catch (e: NullPointerException) {
                Log.e("getAllContactNames()", e.message)
            }

            return lContactNamesList
        }

    private fun initViews(roomView: View?) {
        creditButton = roomView?.findViewById(R.id.CreditButton)
        creditButton?.setOnClickListener(this)
        debitButton = roomView?.findViewById(R.id.DebitButton)
        debitButton?.setOnClickListener(this)
        creditDebitName = roomView?.findViewById(R.id.DebitCreditName)
        creditDebitPrice = roomView?.findViewById(R.id.DebitCreditPrice)
        debitCreditSaveButton = roomView?.findViewById(R.id.DebitCreditSaveButton)
        debitCreditSaveButton?.setOnClickListener(this)
        creditDebitNotes = roomView?.findViewById(R.id.debitCreditNotes)
        backImage = roomView?.findViewById(R.id.backImage)
        backImage?.setOnClickListener(this)
        imageView = roomView?.findViewById(R.id.profile_image)
        imageView!!.setOnClickListener { showPictureDialog() }

        creditDebitName!!.setAdapter(ArrayAdapter(context, R.layout.single_contact, R.id.tv_ContactName, allContactNames))
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !=0)
        {
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                    saveImage(bitmap)
                    imageView!!.setImageURI(contentURI)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            if (data != null) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                imageView!!.setImageBitmap(thumbnail)
                saveImage(thumbnail)
            }
        }}
    }    private var DateAndTime = ""


    private fun getCurretDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy     hh:mm")
        val currentDate = sdf.format(Date())
        if (DateAndTime.isEmpty()) {
            DateAndTime = currentDate
        }
        return DateAndTime
    }
    override fun onClick(p0: View?) {
        val id = p0?.id

        val date = getCurretDateTime().split(" ")

        when (id) {
            R.id.DebitButton -> {
                setButtonBackground(R.drawable.only_border, R.drawable.round_button)
                creditOrDebit = "Debit"
            }
            R.id.CreditButton -> {
                setButtonBackground(R.drawable.round_button, R.drawable.only_border)
                creditOrDebit = "Credit"
            }
            R.id.DebitCreditSaveButton -> {
                if (creditDebitName?.text!!.isNotEmpty() && creditDebitPrice?.text!!.isNotEmpty()) {
                    if (DateString.isEmpty()) {
                        DateString = getDate()
                    }
                    hideKeyboard()
                    debitCreditSaveClick?.OnDabitCreditSaveClick(CreditDebitEntities(ID, creditDebitName?.text.toString(),
                            date[0], creditDebitPrice?.text.toString().toInt(), imageDir, creditOrDebit, creditDebitNotes?.text.toString()))
                    fragmentManager?.popBackStack()
                } else {
                    Toast.makeText(context, "Please fill above field's ", Int.MIN_VALUE).show()
                }
            }
            R.id.backImage -> {
                fragmentManager?.popBackStack()
            }
        }

    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(Date())
    }

    private fun setButtonBackground(only_border: Int, round_button: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            creditButton?.background = resources.getDrawable(only_border)
            debitButton?.background = resources.getDrawable(round_button)
        }
    }

    private fun Fragment.hideKeyboard() {
        activity.hideKeyboard(view)
    }

    private fun FragmentActivity?.hideKeyboard(view: View?) {
        val inputMethodManager = this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            imageDir = f.absolutePath
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/PurchaseImages"
    }



}
