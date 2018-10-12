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
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuqiang ZHENG on 2017/4/9.
 */
public class DragToDismissLayout extends FrameLayout implements HasDismissCallback {

    private List<DragToDismissCallback> callbacks = new ArrayList<>();
    private DragToDismissHelper helper;

    public DragToDismissLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public DragToDismissLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragToDismissLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = DragToDismissHelper.create(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        helper.onHeightChanged(h);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return helper.isVerticalScroll(nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (helper.draggingDown && dy > 0 || helper.draggingUp && dy < 0) {
            helper.dragScale(this, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        helper.dragScale(this, dyUnconsumed);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        helper.lastEventAction = ev.getAction();
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onStopNestedScroll(View child) {
        helper.finishOrCancel(this);
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
