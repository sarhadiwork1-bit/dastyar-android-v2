
package com.example.dastyar

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import android.content.Context

class MainActivity : Activity() {

    private var english = false
    private lateinit var root: LinearLayout
    private lateinit var input: EditText

    private val prefs by lazy {
        getSharedPreferences("dastyar_data", Context.MODE_PRIVATE)
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        english = prefs.getBoolean("english", false)
        buildScreen()
    }

    private fun label(fa: String, en: String) =
        if (english) en else fa

    private fun buildScreen() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(20), dp(20), dp(20))
            setBackgroundColor(Color.rgb(245, 247, 250))
            layoutDirection =
                if (english) View.LAYOUT_DIRECTION_LTR
                else View.LAYOUT_DIRECTION_RTL
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = root.layoutDirection
        }
        scroll.addView(content)

        val languageButton = Button(this).apply {
            text = label("English", "فارسی")
            setOnClickListener {
                english = !english
                prefs.edit().putBoolean("english", english).apply()
                buildScreen()
            }
        }
        content.addView(languageButton)

        val title = TextView(this).apply {
            text = label("دستیار شخصی من", "My Personal Assistant")
            textSize = 27f
            setTextColor(Color.rgb(28, 57, 92))
            gravity = Gravity.CENTER
            setPadding(0, dp(28), 0, dp(12))
        }
        content.addView(title)

        val subtitle = TextView(this).apply {
            text = label(
                "نسخه اول؛ ساده، شخصی و آفلاین",
                "Version 1; simple, personal and offline"
            )
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
        }
        content.addView(subtitle)

        val section = TextView(this).apply {
            text = label("یادداشت جدید", "New Note")
            textSize = 20f
            setTextColor(Color.BLACK)
            setPadding(0, dp(28), 0, dp(8))
        }
        content.addView(section)

        input = EditText(this).apply {
            hint = label(
                "یادداشتت را اینجا بنویس...",
                "Write your note here..."
            )
            minLines = 3
            gravity = Gravity.TOP
            textSize = 16f
        }
        content.addView(
            input,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val saveButton = Button(this).apply {
            text = label("ذخیره یادداشت", "Save Note")
            setOnClickListener {
                val note = input.text.toString().trim()
                if (note.isNotEmpty()) {
                    val old = prefs.getString("notes", "") ?: ""
                    val updated = if (old.isEmpty()) note else "$old\n\n$note"
                    prefs.edit().putString("notes", updated).apply()
                    buildScreen()
                    Toast.makeText(
                        this@MainActivity,
                        label("یادداشت ذخیره شد", "Note saved"),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    input.error = label(
                        "ابتدا یادداشتی بنویس",
                        "Enter a note first"
                    )
                }
            }
        }
        content.addView(saveButton)

        val notesTitle = TextView(this).apply {
            text = label("یادداشت‌های ذخیره‌شده", "Saved Notes")
            textSize = 20f
            setTextColor(Color.BLACK)
            setPadding(0, dp(24), 0, dp(8))
        }
        content.addView(notesTitle)

        val notesView = TextView(this).apply {
            text = prefs.getString(
                "notes",
                label("هنوز یادداشتی نداری.", "No notes yet.")
            )
            textSize = 16f
            setTextColor(Color.DKGRAY)
            setPadding(dp(12), dp(12), dp(12), dp(12))
            setBackgroundColor(Color.WHITE)
        }
        content.addView(
            notesView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )
        setContentView(root)
    }
}
