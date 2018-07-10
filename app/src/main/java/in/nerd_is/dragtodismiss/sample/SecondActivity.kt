/*
 *    Copyright 2018 Xuqiang ZHENG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package `in`.nerd_is.dragtodismiss.sample

import `in`.nerd_is.dragtodismiss.DefaultDismissAnimator
import `in`.nerd_is.dragtodismiss.HasDismissCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class SecondActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val layoutRes = when (intent.action) {
      ACTION_FRAME -> R.layout.activity_second_frame
      ACTION_COORDINATOR -> R.layout.activity_second_coordinator
      else -> throw IllegalArgumentException("Unknown action")
    }
    setContentView(layoutRes)

    val view = findViewById<View>(R.id.dragToDismissLayout) as HasDismissCallback
    view.addListener(DefaultDismissAnimator(this))
  }

  companion object {
    const val ACTION_FRAME = "action_frame"
    const val ACTION_COORDINATOR = "action_coordinator"

    fun startFrame(context: Context) {
      val intent = Intent(context, SecondActivity::class.java)
      intent.action = ACTION_FRAME
      context.startActivity(intent)
    }

    fun startCoordinator(context: Context) {
      val intent = Intent(context, SecondActivity::class.java)
      intent.action = ACTION_COORDINATOR
      context.startActivity(intent)
    }
  }
}
