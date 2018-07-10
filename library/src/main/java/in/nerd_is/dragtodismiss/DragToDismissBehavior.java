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
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Xuqiang ZHENG on 18/7/9.
 */
public class DragToDismissBehavior extends CoordinatorLayout.Behavior<View> {

    private DragToDismissHelper helper;

    public DragToDismissBehavior() {
        super();
    }

    public DragToDismissBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        helper = DragToDismissHelper.create(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return isTouch(type) && helper.isVerticalScroll(axes);
    }

    @Override
    public void onNestedPreScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
            @NonNull View target,
            int dx,
            int dy,
            @NonNull int[] consumed,
            int type) {
        if (!isTouch(type)) {
            return;
        }

        if (helper.draggingDown && dy > 0 || helper.draggingUp && dy < 0) {
            helper.dragScale(coordinatorLayout, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
            @NonNull View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed,
            int type) {
        if (!isTouch(type)) {
            return;
        }
        helper.dragScale(coordinatorLayout, dyUnconsumed);
    }

    @Override
    public boolean onInterceptTouchEvent(
            @NonNull CoordinatorLayout parent,
            @NonNull View child,
            @NonNull MotionEvent ev) {
        helper.lastEventAction = ev.getAction();
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onStopNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
            @NonNull View target,
            int type) {
        if (!isTouch(type)) {
            return;
        }
        helper.finishOrCancel(coordinatorLayout);
    }

    private boolean isTouch(int type) {
        return type == ViewCompat.TYPE_TOUCH;
    }
}