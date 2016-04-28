package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Scroller;

import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.widget.TimeTable;

/**
 * Created by ybao on 16/4/27.
 */
public class ScheduleTimeLayout extends LinearLayout{

    private Scroller mScroller;

    public ScheduleTimeLayout(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public boolean isXScrollOut(int scrollX){
        if(scrollX < 0 || scrollX > TimeTable.WEEK_DAY_WIDTH * 7){
            return true;
        }else return false;
    }
    
//    public boolean isYScrollOut(int scrollY){
//        if (scrollY < 0 )
//    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        Logger.d(getScrollX() + "");
        Logger.d(getScrollY() + "");
        Logger.d(DimensUtil.getScreenHeight() + "");
        x = checkPositionX(x);
        y = checkPositionY(y);
        super.scrollBy(x,y);
    }

    public int checkPositionX(int x) {
        if (getScrollX() + x < 0) {
            x = - getScrollX();
        } else if ((getScrollX() + x) >
                (TimeTable.WEEK_DAY_WIDTH * 7 - DimensUtil.getScreenWidth() + TimeTable.LITTLE_VIEW_WIDTH)) {
            x = TimeTable.WEEK_DAY_WIDTH * 7 - getScrollX() - DimensUtil.getScreenWidth() + TimeTable.LITTLE_VIEW_WIDTH;
        }
        return x;
    }

    public int checkPositionY(int y){
        if (getScrollY() + y < 0) {
            y = - getScrollY();
        } else if ((getScrollY() + y) >
                (TimeTable.COURSE_TIME_HEIGHT * 7 - DimensUtil.getScreenHeight() + DimensUtil.getToolbarHeight()+ DimensUtil.getStatusBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT)) {
            y = TimeTable.COURSE_TIME_HEIGHT * 7 - getScrollY() - DimensUtil.getScreenHeight() + DimensUtil.getToolbarHeight() + DimensUtil.getNavigationBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT;
        }
        return y;
    }

    public void smoothScrollBy(int deltaX, int deltaY) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        deltaX = checkPositionX(deltaX);
        deltaY = checkPositionY(deltaY);
        mScroller.startScroll(scrollX, scrollY, deltaX , deltaY , 1000);
    }
}