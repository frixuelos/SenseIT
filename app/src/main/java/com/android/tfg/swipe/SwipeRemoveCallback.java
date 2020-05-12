package com.android.tfg.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;

public class SwipeRemoveCallback extends ItemTouchHelper.Callback {

    private Context context;
    private Paint removePaint;
    private ColorDrawable background;
    private int backgroundColor;
    private Drawable removeDrawable;
    private int width;
    private int height;


    public SwipeRemoveCallback(Context context){
        this.context=context;
        this.background=new ColorDrawable();
        this.backgroundColor=context.getResources().getColor(R.color.colorError);
        this.removePaint=new Paint();
        this.removePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.removeDrawable=context.getResources().getDrawable(R.drawable.ic_delete_sweep_24dp);
        this.width=150;//removeDrawable.getIntrinsicWidth();
        this.height=150;//removeDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if(isCancelled){
            clearCanvas(c, itemView.getRight()+dX, (float) itemView.getTop(), (float) itemView.getRight(), (float)itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        background.setColor(backgroundColor);
        background.setBounds(itemView.getRight()+ (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        int deleteIconTop = itemView.getTop() + (itemHeight-height)/2;
        int deleteIconMargin = (itemHeight-height)/2;
        int deleteIconLeft = itemView.getRight()-deleteIconMargin-width;
        int deleteIconRight = itemView.getRight()-deleteIconMargin;
        int deleteIconBottom = deleteIconTop + height;

        int scale=-20;
        removeDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        removeDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom){
        c.drawRect(left, top, right, bottom, removePaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}
