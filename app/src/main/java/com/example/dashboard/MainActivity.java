package com.example.dashboard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressPercentage;
    private int progress = 0;

    // Task count TextViews
    private TextView fitnessCount, workCount, healthCount, learningCount;
    private TextView hobbiesCount, travelCount, socialCount, creativityCount;
    private TextView recentTasksTextView;

    // Task counts
    private int fitnessTaskCount = 0, workTaskCount = 0, healthTaskCount = 0, learningTaskCount = 0;
    private int hobbiesTaskCount = 0, travelTaskCount = 0, socialTaskCount = 0, creativityTaskCount = 0;

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> recentActivities;
    private List<Long> activityTimestamps;

    private FloatingActionButton fab;
    private int[] fabColors;
    private int currentColorIndex = 0;

    // Task icon ImageViews
    private ImageView fitnessIcon, workIcon, healthIcon, learningIcon;
    private ImageView hobbiesIcon, travelIcon, socialIcon, creativityIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Progress bar and percentage display
        progressBar = findViewById(R.id.progressBar);
        progressPercentage = findViewById(R.id.progressPercentage);

        // FAB color setup
        fabColors = new int[]{
                getResources().getColor(R.color.fabColor1),
                getResources().getColor(R.color.fabColor2),
                getResources().getColor(R.color.fabColor3),
                getResources().getColor(R.color.fabColor4),
                getResources().getColor(R.color.fabColor5),
                getResources().getColor(R.color.fabColor6)
        };

        // Restoring state if available
        if (savedInstanceState != null) {
            progress = savedInstanceState.getInt("progress", 0);
            fitnessTaskCount = savedInstanceState.getInt("fitnessCount", 0);
            workTaskCount = savedInstanceState.getInt("workCount", 0);
            healthTaskCount = savedInstanceState.getInt("healthCount", 0);
            learningTaskCount = savedInstanceState.getInt("learningCount", 0);
            hobbiesTaskCount = savedInstanceState.getInt("hobbiesCount", 0);
            travelTaskCount = savedInstanceState.getInt("travelCount", 0);
            socialTaskCount = savedInstanceState.getInt("socialCount", 0);
            creativityTaskCount = savedInstanceState.getInt("creativityCount", 0);
            recentActivities = savedInstanceState.getStringArrayList("recentActivities");
            activityTimestamps = (List<Long>) savedInstanceState.getSerializable("activityTimestamps");
        } else {
            recentActivities = new ArrayList<>();
            activityTimestamps = new ArrayList<>();
        }

        progressBar.setProgress(progress);
        progressPercentage.setText(progress + "%");

        // Initialize task TextViews
        fitnessCount = findViewById(R.id.fitnessCount);
        workCount = findViewById(R.id.workCount);
        healthCount = findViewById(R.id.healthCount);
        learningCount = findViewById(R.id.learningCount);
        hobbiesCount = findViewById(R.id.hobbiesCount);
        travelCount = findViewById(R.id.travelCount);
        socialCount = findViewById(R.id.socialCount);
        creativityCount = findViewById(R.id.creativityCount);

        // Initialize task icon ImageViews
        fitnessIcon = findViewById(R.id.fitnessIcon);
        workIcon = findViewById(R.id.workIcon);
        healthIcon = findViewById(R.id.healthIcon);
        learningIcon = findViewById(R.id.learningIcon);
        hobbiesIcon = findViewById(R.id.hobbiesIcon);
        travelIcon = findViewById(R.id.travelIcon);
        socialIcon = findViewById(R.id.socialIcon);
        creativityIcon = findViewById(R.id.creativityIcon);

        updateAllTaskCounts();

        recentTasksTextView = findViewById(R.id.recentTasksTextView);
        if (!recentActivities.isEmpty()) {
            updateRecentTasksTextView();
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            animateFab(v);
            incrementAllTaskCounts();
        });

        // Setting onClick listeners for task icons to highlight them when pressed
        fitnessIcon.setOnClickListener(v -> highlightTaskIcon(fitnessIcon));
        workIcon.setOnClickListener(v -> highlightTaskIcon(workIcon));
        healthIcon.setOnClickListener(v -> highlightTaskIcon(healthIcon));
        learningIcon.setOnClickListener(v -> highlightTaskIcon(learningIcon));
        hobbiesIcon.setOnClickListener(v -> highlightTaskIcon(hobbiesIcon));
        travelIcon.setOnClickListener(v -> highlightTaskIcon(travelIcon));
        socialIcon.setOnClickListener(v -> highlightTaskIcon(socialIcon));
        creativityIcon.setOnClickListener(v -> highlightTaskIcon(creativityIcon));
    }

    private void refreshContent() {
        resetTaskCounts();
        updateRecentTasksTextView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateFab(View fab) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        fab.startAnimation(rotateAnimation);

        currentColorIndex = (currentColorIndex + 1) % fabColors.length;
        fab.setBackgroundTintList(ColorStateList.valueOf(fabColors[currentColorIndex]));
    }

    private void updateTaskCount(TextView taskCountView, int newCount) {
        taskCountView.setText(String.valueOf(newCount));
    }

    private void incrementAllTaskCounts() {
        fitnessTaskCount++;
        workTaskCount++;
        healthTaskCount++;
        learningTaskCount++;
        hobbiesTaskCount++;
        travelTaskCount++;
        socialTaskCount++;
        creativityTaskCount++;

        progress++;
        progressBar.setProgress(progress);
        progressPercentage.setText(progress + "%");

        String activity = "ADDED tasks: " + fitnessTaskCount + " Fitness, " +
                workTaskCount + " Work, " +
                healthTaskCount + " Health, " +
                learningTaskCount + " Learning, " +
                hobbiesTaskCount + " Hobbies, " +
                travelTaskCount + " Travel, " +
                socialTaskCount + " Social, " +
                creativityTaskCount + " Creativity";

        long currentTime = System.currentTimeMillis();
        recentActivities.add(0, activity);
        activityTimestamps.add(0, currentTime);

        updateRecentTasksTextView();
        updateAllTaskCounts();
    }

    private void updateAllTaskCounts() {
        updateTaskCount(fitnessCount, fitnessTaskCount);
        updateTaskCount(workCount, workTaskCount);
        updateTaskCount(healthCount, healthTaskCount);
        updateTaskCount(learningCount, learningTaskCount);
        updateTaskCount(hobbiesCount, hobbiesTaskCount);
        updateTaskCount(travelCount, travelTaskCount);
        updateTaskCount(socialCount, socialTaskCount);
        updateTaskCount(creativityCount, creativityTaskCount);
    }

    private void highlightTaskIcon(ImageView taskIcon) {
        taskIcon.setColorFilter(getResources().getColor(R.color.highlightColor)); // Apply highlight color

        // Remove highlight after a short delay
        taskIcon.postDelayed(() -> taskIcon.clearColorFilter(), 500);
    }

    private void updateRecentTasksTextView() {
        StringBuilder recentTasksBuilder = new StringBuilder();
        for (int i = 0; i < recentActivities.size(); i++) {
            String activity = recentActivities.get(i);
            long timestamp = activityTimestamps.get(i);
            long minutesAgo = (System.currentTimeMillis() - timestamp) / (1000 * 60);
            recentTasksBuilder.append(activity)
                    .append(" (").append(minutesAgo).append(" min ago)\n");
        }
        recentTasksTextView.setText(recentTasksBuilder.toString());
    }

    private void resetTaskCounts() {
        fitnessTaskCount = 0;
        workTaskCount = 0;
        healthTaskCount = 0;
        learningTaskCount = 0;
        hobbiesTaskCount = 0;
        travelTaskCount = 0;
        socialTaskCount = 0;
        creativityTaskCount = 0;

        progress = 0;
        progressBar.setProgress(progress);
        progressPercentage.setText(progress + "%");
        updateAllTaskCounts();
        recentActivities.clear();
        activityTimestamps.clear();
        recentTasksTextView.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress", progress);
        outState.putInt("fitnessCount", fitnessTaskCount);
        outState.putInt("workCount", workTaskCount);
        outState.putInt("healthCount", healthTaskCount);
        outState.putInt("learningCount", learningTaskCount);
        outState.putInt("hobbiesCount", hobbiesTaskCount);
        outState.putInt("travelCount", travelTaskCount);
        outState.putInt("socialCount", socialTaskCount);
        outState.putInt("creativityCount", creativityTaskCount);
        outState.putStringArrayList("recentActivities", new ArrayList<>(recentActivities));
        outState.putSerializable("activityTimestamps", (ArrayList<Long>) activityTimestamps);
    }
}
