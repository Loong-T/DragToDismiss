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
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * @author Xuqiang ZHENG on 2017/4/19.
 */
@SuppressWarnings("UnusedAssignment")
public class DragToDismissHelper {

    public boolean draggingDown = false;
    public boolean draggingUp = false;
    public int lastEventAction;

    private float totalDrag;

    private float distance = Float.MAX_VALUE;
    private float fraction = -1f;
    private float elasticity = 0.8f;
    private float scale = 1f;
    private boolean shouldScale = false;

    private DragToDismissHelper(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.DragToDismiss, 0, 0);

        distance = a.getDimension(R.styleable
                .DragToDismiss_dtd_dismissDistance, distance);
        fraction = a.getFloat(R.styleable
                .DragToDismiss_dtd_dismissFraction, fraction);
        elasticity = a.getFloat(R.styleable
                .DragToDismiss_dtd_elasticity, elasticity);
        scale = a.getFloat(R.styleable
                .DragToDismiss_dtd_dismissScale, scale);
        shouldScale = scale != 1f;

        a.recycle();
    }

    public static DragToDismissHelper create(@NonNull Context context,
                                             @Nullable AttributeSet attrs) {
        return new DragToDismissHelper(context, attrs);
    }

    public boolean isVerticalScroll(int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    public void dragScale(View view, int scroll) {
        if (scroll == 0) {
            return;
        }

        totalDrag += scroll;
        float absTotalDrag = Math.abs(totalDrag);

        // track the direction & set the pivot point for scaling
        // don't double track i.e. if start dragging down and then reverse, keep tracking as
        // dragging down until they reach the 'natural' position
        if (!draggingUp && !draggingDown) {
            if (scroll < 0) {
                draggingDown = true;
                if (shouldScale) {
                    view.setPivotY(view.getHeight());
                }
            } else {
                draggingUp = true;
                if (shouldScale) {
                    view.setPivotY(0f);
                }
            }
        }
        // how far have we dragged relative to the distance to perform a dismiss
        // (0–1 where 1 = dismiss distance). Decreasing logarithmically as we approach the limit
        float dragFraction = (float) Math.log10(1 + (absTotalDrag / distance));

        // calculate the desired translation given the drag fraction
        float dragTo = dragFraction * distance * elasticity;

        if (draggingUp) {
            // as we use the absolute magnitude when calculating the drag fraction, need to
            // re-apply the drag direction
            dragTo *= -1;
        }
        view.setTranslationY(dragTo);

        if (shouldScale && absTotalDrag > distance) {
            // 1 = log2(1 + distance / distance)
            final float scale = 1 - ((1 - this.scale) * dragFraction);
            view.setScaleX(scale);
            view.setScaleY(scale);
        }

        // if we've reversed direction and gone past the settle point then clear the flags to
        // allow the list to get the scroll events & reset any transforms
        if ((draggingDown && totalDrag >= 0)
                || (draggingUp && totalDrag <= 0)) {
            totalDrag = 0;
            dragTo = 0;
            dragFraction = 0;
            draggingDown = false;
            draggingUp = false;
            view.setTranslationY(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        }

        dispatchDragCallback(view, dragFraction, dragTo,
                Math.min(1f, Math.abs(totalDrag) / distance), totalDrag);
    }

    public void finishOrCancel(View view) {
        if (Math.abs(totalDrag) >= distance) {
            dispatchDismissCallback(view);
        } else { // settle back to natural position
            if (lastEventAction == MotionEvent.ACTION_DOWN) {
                // this is a 'defensive cleanup for new gestures',
                // don't animate here
                // see also https://github.com/nickbutcher/plaid/issues/185
                view.setTranslationY(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else {
                view.animate()
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200L)
                        .start();
            }
            totalDrag = 0;
            draggingUp = false;
            draggingDown = false;
            dispatchDragCallback(view, 0f, 0f, 0f, 0f);
        }
    }

    public void onHeightChanged(int h) {
        if (fraction > 0f) {
            distance = h * fraction;
        }
    }

    private void dispatchDragCallback(View view, float elasticOffset, float elasticOffsetPixels,
                                      float rawOffset, float rawOffsetPixels) {
        if (view instanceof HasDismissCallback) {
            List<DragToDismissCallback> callbacks = ((HasDismissCallback) view).getDismissCallbacks();
            if (!callbacks.isEmpty()) {
                for (DragToDismissCallback callback : callbacks) {
                    callback.onDrag(elasticOffset, elasticOffsetPixels,
                            rawOffset, rawOffsetPixels);
                }
            }
        }
    }

    private void dispatchDismissCallback(View view) {
        if (view instanceof HasDismissCallback) {
            List<DragToDismissCallback> callbacks = ((HasDismissCallback) view).getDismissCallbacks();
            if (!callbacks.isEmpty()) {
                for (DragToDismissCallback callback : callbacks) {
                    callback.onDragDismissed();
                }
            }
        }
    }
}
