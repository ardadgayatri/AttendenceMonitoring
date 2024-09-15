package com.example.attendencemonitering;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
   bottomNavigationView =findViewById(R.id.homeBottomNavigationView);
   bottomNavigationView.setOnNavigationItemSelectedListener(this);
   bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuClass);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.homeMenuSave)
        {

        } else if (item.getItemId()== R.id.homeMenuEdit)
        {

        } else if (item.getItemId()== R.id.homeMenuMyProfile) {
            Intent intent= new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);

        }
        return true;
    }

    ClassFragment classFragment =new ClassFragment();
    AttendenceFragment attendenceFragment = new AttendenceFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.homeBottomNavigationMenuClass)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,classFragment).commit();
        } else if (item.getItemId()==R.id.homeBottomNavigationMenuAttendence) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,attendenceFragment).commit();
        } else if (item.getItemId()==R.id.homeBottomNavigationMenuStatistics) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,statisticsFragment).commit();
        }


        return true;
    }
}