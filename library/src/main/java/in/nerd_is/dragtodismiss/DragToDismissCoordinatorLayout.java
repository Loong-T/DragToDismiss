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

package in.nerd_is.dragtodismiss;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Xuqiang ZHENG on 2017/4/7.
 */
public class DragToDismissCoordinatorLayout extends CoordinatorLayout implements HasDismissCallback {

    @NonNull private List<DragToDismissCallback> callbacks = new ArrayList<>();

    public DragToDismissCoordinatorLayout(Context context) {
        super(context);
    }

    public DragToDismissCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragToDismissCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public List<DragToDismissCallback> getDismissCallbacks() {
        return callbacks;
    }

    @Override
    public void addListener(DragToDismissCallback listener) {
        callbacks.add(listener);
    }

    @Override
    public void removeListener(DragToDismissCallback listener) {
        if (callbacks.size() > 0) {
            callbacks.remove(listener);
        }
    }
}
