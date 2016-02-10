package kidouchi.chronobook;

/**
 * Created by iuy407 on 2/2/16.
 *
 * Notifies View Holder of relevant callback from
 * {@link android.support.v7.widget.helper.ItemTouchHelper.Callback}
 *
 */
public interface EventCardTouchViewHolder {

    /**
     * Called when {@link android.support.v7.widget.helper.ItemTouchHelper}
     * first registers and item as being swiped/moved.
     * Updates item view to indicate it's active state
     */
    void onItemSelected();

    /**
     * Called when {@link android.support.v7.widget.helper.ItemTouchHelper}
     * has complete the swipe/move motion and active item state is cleared
     */
    void onItemClear();
}
