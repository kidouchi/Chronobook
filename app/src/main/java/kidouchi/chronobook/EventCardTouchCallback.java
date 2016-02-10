package kidouchi.chronobook;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by iuy407 on 2/1/16.
 */
public class EventCardTouchCallback extends ItemTouchHelper.Callback {

    private final EventCardTouchHelperAdapter mTouchAdapter;

    private boolean wasFullySwiped = false;

    public EventCardTouchCallback(EventCardTouchHelperAdapter touchAdapter) {
        mTouchAdapter = touchAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder instanceof EventCardTouchViewHolder) {
                ((EventCardTouchViewHolder) viewHolder).onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (wasFullySwiped) {
            Snackbar snackbarUndo =
                    Snackbar.make(recyclerView, "An event was dismissed", Snackbar.LENGTH_INDEFINITE)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int pos = viewHolder.getAdapterPosition();
                                    mTouchAdapter.onItemDismissUndone(pos);
                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(recyclerView.getContext(),
                                    R.color.undo_button_color))
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {

                                    super.onDismissed(snackbar, event);
                                }
                            });
            snackbarUndo.show();
        } else {
            if (viewHolder instanceof EventCardTouchViewHolder) {
                ((EventCardTouchViewHolder) viewHolder).onItemClear();
            }
        }
        wasFullySwiped = false; // reset swiped var
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        // Animate fade out as user slides card
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
            float width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        wasFullySwiped = true;
        mTouchAdapter.onItemDismiss(viewHolder.getAdapterPosition(), viewHolder.itemView);
    }
}
