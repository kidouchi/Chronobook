package kidouchi.chronobook;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by iuy407 on 2/1/16.
 */
public class EventCardTouchCallback extends ItemTouchHelper.Callback {

    private final EventCardTouchHelperAdapter mAdapter;

    public EventCardTouchCallback(EventCardTouchHelperAdapter adapter) {
        mAdapter = adapter;
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
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof EventCardTouchViewHolder) {
            ((EventCardTouchViewHolder) viewHolder).onItemClear();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof EventCardTouchViewHolder) {
            ((EventCardTouchViewHolder) viewHolder).onItemSwiped();
        }
        mAdapter.onItemRemove(viewHolder.getAdapterPosition());
    }
}
