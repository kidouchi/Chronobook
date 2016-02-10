package kidouchi.chronobook;

import android.view.View;

/**
 * Created by iuy407 on 2/1/16.
 */
public interface EventCardTouchHelperAdapter {

    /**
     * When item is fully swiped off then show {@link android.support.design.widget.Snackbar}
     * User can undo or dismiss item
     * @param pos position in {@link android.support.v7.widget.RecyclerView} events list
     */
    void onItemDismiss(int pos, View itemView);

    /**
     * When a user dismisses and item but undoes the action
     * @param pos
     */
    void onItemDismissUndone(int pos);
}
