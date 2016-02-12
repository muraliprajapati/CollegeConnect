package com.sophomoreventure.collegeconnect;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.Network.AboutActivity;

import java.util.ArrayList;

/**
 * Created by Murali on 06/02/2016.
 */
public abstract class DrawerBaseActivity extends AppCompatActivity {
    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_SPARSH_EVENTS = 0;
    protected static final int NAVDRAWER_ITEM_COLLEGE_EVENTS = 1;
    protected static final int NAVDRAWER_ITEM_COLLEGE_CLUBS = 2;
    protected static final int NAVDRAWER_ITEM_NOTICE_BOARD = 3;
    protected static final int NAVDRAWER_ITEM_CREATE_EVENT = 4;
    protected static final int NAVDRAWER_ITEM_CREATE_NOTICE = 5;
    protected static final int NAVDRAWER_ITEM_MY_EVENTS = 6;
    protected static final int NAVDRAWER_ITEM_FOLLOWED_EVENTS = 7;

    //    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();
    protected static final int NAVDRAWER_ITEM_ABOUT = 8;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;
    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;
    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_sparsh_events,
            R.string.navdrawer_item_college_events,
            R.string.navdrawer_item_college_clubs,
            R.string.navdrawer_item_notice_board,
            R.string.navdrawer_item_create_event,
            R.string.navdrawer_item_create_notice,
            R.string.navdrawer_item_my_events,
            R.string.navdrawer_item_followed_events,
            R.string.navdrawer_item_about,
    };
    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_receipt_blue_grey_300_24dp,  // Sparsh events
            R.drawable.ic_receipt_blue_grey_300_24dp, // College events
            R.drawable.ic_supervisor_account_blue_grey_300_24dp,  // Clubs
            R.drawable.ic_receipt_blue_grey_300_24dp, // notice board
            R.drawable.ic_add_circle_blue_grey_500_24dp, // create event
            R.drawable.ic_add_circle_blue_grey_500_24dp, // create notice
            R.drawable.ic_loyalty_blue_grey_300_24dp, // created events
            R.drawable.ic_loyalty_blue_grey_300_24dp, // followed events.
            R.drawable.ic_info_blue_grey_300_24dp, // About
    };
    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 350;
    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 350;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    private DrawerLayout mDrawerLayout;
    private ObjectAnimator mStatusBarColorAnimator;
    private ViewGroup mDrawerItemsListContainer;
    private Handler mHandler;
    private Runnable mDeferredOnDrawerClosedRunnable;
    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;
    private boolean mActionBarShown = true;
    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();
    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;


    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;
    private int mThemedStatusBarColor;
    private int mNormalStatusBarColor;

    /**
     * This utility method handles Up navigation intents by searching for a parent activity and
     * navigating there if defined. When using this for an activity make sure to define both the
     * native parentActivity as well as the AppCompat one when supporting API levels less than 16.
     * when the activity has a single parent activity. If the activity doesn't have a single parent
     * activity then don't define one and this method will use back button functionality. If "Up"
     * functionality is still desired for activities without parents then use
     * {@code syntheticParentActivity} to define one dynamically.
     * <p/>
     * Note: Up navigation intents are represented by a back arrow in the top left of the Toolbar
     * in Material Design guidelines.
     *
     * @param currentActivity         Activity in use when navigate Up action occurred.
     * @param syntheticParentActivity Parent activity to use when one is not already configured.
     */
    public static void navigateUpOrBack(Activity currentActivity,
                                        Class<? extends Activity> syntheticParentActivity) {
        // Retrieve parent activity from AndroidManifest.
        Intent intent = NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if (intent == null && syntheticParentActivity != null) {
            try {
                intent = NavUtils.getParentActivityIntent(currentActivity, syntheticParentActivity);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (intent == null) {
            // No parent defined in manifest. This indicates the activity may be used by
            // in multiple flows throughout the app and doesn't have a strict parent. In
            // this case the navigation up button should act in the same manner as the
            // back button. This will result in users being forwarded back to other
            // applications if currentActivity was invoked from another application.
            currentActivity.onBackPressed();
        } else {
            if (NavUtils.shouldUpRecreateTask(currentActivity, intent)) {
                // Need to synthesize a backstack since currentActivity was probably invoked by a
                // different app. The preserves the "Up" functionality within the app according to
                // the activity hierarchy defined in AndroidManifest.xml via parentActivity
                // attributes.
                TaskStackBuilder builder = TaskStackBuilder.create(currentActivity);
                builder.addNextIntentWithParentStack(intent);
                builder.startActivities();
            } else {
                // Navigate normally to the manifest defined "Up" activity.
                NavUtils.navigateUpTo(currentActivity, intent);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemedStatusBarColor = getResources().getColor(R.color.colorPrimaryDark);
        mNormalStatusBarColor = mThemedStatusBarColor;
        mHandler = new Handler();
    }

    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.colorPrimaryDark));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }
        if (navDrawer != null) {
            ((TextView) findViewById(R.id.drawer_user_name))
                    .setText(EventUtility.getUserNameFromPref(this));
            ((TextView) findViewById(R.id.drawer_user_email))
                    .setText(EventUtility.getUserEmailFromPref(this));
            ((TextView) findViewById(R.id.drawer_user_roll_no))
                    .setText(EventUtility.getUserRollNoFromPref(this));

        }

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide(slideOffset);
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (EventUtility.isFirstRun(DrawerBaseActivity.this)) {
            // first run of the app starts with the nav drawer open
            EventUtility.markFirstRunDone(this, false);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Defines the Navigation Drawer items to display by updating {@code mNavDrawerItems} then
     * forces the Navigation Drawer to redraw itself.
     */
    private void populateNavDrawer() {

        mNavDrawerItems.clear();

        // decide which items will appear in the nav drawer
        mNavDrawerItems.add(NAVDRAWER_ITEM_SPARSH_EVENTS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        // Explore is always shown.
        mNavDrawerItems.add(NAVDRAWER_ITEM_COLLEGE_EVENTS);




//        mNavDrawerItems.add(NAVDRAWER_ITEM_NOTICE_BOARD);
        mNavDrawerItems.add(NAVDRAWER_ITEM_COLLEGE_CLUBS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        if (EventUtility.isUserVerified(this)) {
            mNavDrawerItems.add(NAVDRAWER_ITEM_CREATE_EVENT);
            mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        }
//        mNavDrawerItems.add(NAVDRAWER_ITEM_CREATE_NOTICE);

        mNavDrawerItems.add(NAVDRAWER_ITEM_FOLLOWED_EVENTS);
        if (EventUtility.isUserVerified(this)) {
            mNavDrawerItems.add(NAVDRAWER_ITEM_MY_EVENTS);
            mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        }

        mNavDrawerItems.add(NAVDRAWER_ITEM_ABOUT);

        createNavDrawerItems();
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected void onNavDrawerSlide(float offset) {
    }

    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if (mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        if (isSeparator(itemId)) {
            View separator =
                    getLayoutInflater().inflate(R.layout.navdrawer_separator, container, false);
            return separator;
        }

        NavDrawerItemView item = (NavDrawerItemView) getLayoutInflater().inflate(
                R.layout.navdrawer_item, container, false);
        item.setContent(NAVDRAWER_ICON_RES_ID[itemId], NAVDRAWER_TITLE_RES_ID[itemId]);
        item.setActivated(getSelfNavDrawerItem() == itemId);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });
        return item;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

//             launch the target Activity after a short delay, to allow the close animation to play
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(itemId);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        // change the active item on the list so the user can see the item changed
        setSelectedNavDrawerItem(itemId);
        // fade out the main content
        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }


        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void goToNavDrawerItem(int item) {
        switch (item) {
            case NAVDRAWER_ITEM_SPARSH_EVENTS:
                createBackStack(new Intent(this, SparshEventListAtivity.class));
                break;
            case NAVDRAWER_ITEM_COLLEGE_EVENTS:
                startActivity(new Intent(this, SlideShowActivity.class));

                break;
            case NAVDRAWER_ITEM_COLLEGE_CLUBS:
                createBackStack(new Intent(this, ClubListAtivity.class));
                break;
            case NAVDRAWER_ITEM_NOTICE_BOARD:
                createBackStack(new Intent(this, NoticeBoardActivity.class));
                break;
            case NAVDRAWER_ITEM_CREATE_EVENT:
                createBackStack(new Intent(this, CreateEventActivity.class));
                break;
            case NAVDRAWER_ITEM_CREATE_NOTICE:
                createBackStack(new Intent(this, CreateEventActivity.class));
                break;
            case NAVDRAWER_ITEM_FOLLOWED_EVENTS:
                createBackStack(new Intent(this, MyEventsActivity.class));
                break;
            case NAVDRAWER_ITEM_MY_EVENTS:
                createBackStack(new Intent(this, MyEventsActivity.class));
                break;
            case NAVDRAWER_ITEM_ABOUT:
                createBackStack(new Intent(this, AboutActivity.class));
                break;

        }
    }

    private void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.addParentStack(SlideShowActivity.class);
            builder.startActivities();

        } else {
            startActivity(intent);

        }
//        startActivity(intent);

    }

    private boolean isSpecialItem(int itemId) {
//        return itemId == NAVDRAWER_ITEM_SETTINGS;
        return false;
    }

    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    mNavDrawerItemViews[i].setActivated(itemId == thisItemId);
                }
            }
        }
    }

    public int getThemedStatusBarColor() {
        return mThemedStatusBarColor;
    }

    public void setNormalStatusBarColor(int color) {
        mNormalStatusBarColor = color;
        if (mDrawerLayout != null) {
            mDrawerLayout.setStatusBarBackgroundColor(mNormalStatusBarColor);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            Log.i("tag", "No view with ID main_content to fade in.");
        }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}

