package com.example.notesapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeHelperCallback extends ItemTouchHelper.Callback {
    private final Context context;
    private final Drawable editIcon;
    private final Drawable deleteIcon;
    private final ColorDrawable editBackground;
    private final ColorDrawable deleteBackground;

    public SwipeHelperCallback(Context context) {
        this.context = context;
        editIcon = ContextCompat.getDrawable(context, R.drawable.edit);
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete);
        editBackground = new ColorDrawable(Color.GREEN);
        deleteBackground = new ColorDrawable(Color.RED);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) { // Swiping to the right
            editBackground.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            editBackground.draw(c);

            int iconMargin = (itemView.getHeight() - editIcon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - editIcon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + editIcon.getIntrinsicHeight();
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + editIcon.getIntrinsicWidth();

            editIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            editIcon.draw(c);
        } else if (dX < 0) { // Swiping to the left
            deleteBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            deleteBackground.draw(c);

            int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
            int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;

            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            deleteIcon.draw(c);
        } else {
            editBackground.setBounds(0, 0, 0, 0);
            deleteBackground.setBounds(0, 0, 0, 0);
        }
    }
}

