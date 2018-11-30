package edu.floridapoly.polycamsportal.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int upper_query_limit = 50;


    public static final String DATABASE_NAME = "Schedules.db";
    public static final String USER_TABLE_NAME = "user_table";
    public static final String USER_COL_ID = "ID";
    public static final String USER_COL_NAME = "name";
    public static final String USER_COL_FAVSCHED = "favoriteschedule";

    public static final String COURSE_TABLE_NAME = "course_table";
    public static final String COURSE_COL_ID = "ID";
    public static final String COURSE_COL_NAME = "name";
    public static final String COURSE_COL_PROF = "professor";
    public static final String COURSE_COL_START = "starttime";
    public static final String COURSE_COL_END = "endtime";
    public static final String COURSE_COL_ROOM = "room";

    public static final String SCHEDULE_TABLE_NAME = "schedule_table";
    public static final String SCHEDULE_COL_ID = "ID";
    public static final String SCHEDULE_COL_USER = "user";
    public static final String SCHEDULE_COL_NAME = "name";

    public static final String COURSE_LIST_TABLE_NAME = "course_list_table";
    public static final String COURSE_LIST_COL_ID = "ID";
    public static final String COURSE_LIST_COL_USER = "user";
    public static final String COURSE_LIST_COL_SCHED = "schedule";
    public static final String COURSE_LIST_COL_COURSE = "course";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE_NAME +
            " (" + USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COL_NAME + " TEXT NOT NULL UNIQUE, " +
            USER_COL_FAVSCHED + " TEXT " +
            ")");

        db.execSQL("create table " + COURSE_TABLE_NAME +
            " (" + COURSE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURSE_COL_NAME + " TEXT NOT NULL UNIQUE, " +
            COURSE_COL_PROF + " TEXT, " +
            COURSE_COL_START + " TEXT, " +
            COURSE_COL_END + " TEXT, " +
            COURSE_COL_ROOM + " TEXT " +
            ")");
        db.execSQL("create table " + SCHEDULE_TABLE_NAME +
            " (" + SCHEDULE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SCHEDULE_COL_USER + " INTEGER NOT NULL, " +
            USER_COL_NAME + " TEXT NOT NULL UNIQUE, " +
            " FOREIGN KEY (" + SCHEDULE_COL_USER + ") REFERENCES " + USER_TABLE_NAME + " (" + USER_COL_ID + ")" +
            ")");
        db.execSQL("create table " + COURSE_LIST_TABLE_NAME +
            " (" + COURSE_LIST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURSE_LIST_COL_USER + " INTEGER NOT NULL, " +
            COURSE_LIST_COL_SCHED + " INTEGER NOT NULL, " +
            COURSE_LIST_COL_COURSE + " INTEGER NOT NULL, " +
            " FOREIGN KEY (" + COURSE_LIST_COL_USER + ") REFERENCES " + USER_TABLE_NAME + " (" + USER_COL_ID + "), " +
            " FOREIGN KEY (" + COURSE_LIST_COL_SCHED + ") REFERENCES " + SCHEDULE_TABLE_NAME + " (" + COURSE_COL_ID +
            "), " +
            " FOREIGN KEY (" + COURSE_LIST_COL_COURSE + ") REFERENCES " + COURSE_TABLE_NAME + " (" + SCHEDULE_COL_ID + ")" +
            ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ COURSE_LIST_TABLE_NAME);
        onCreate(db);
    }


    public boolean insertUser(UserItem user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_NAME, user.getName());
        contentValues.put(USER_COL_FAVSCHED, user.getFavoriteSchedule());
        long result = db.insert(USER_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertCourse(CourseItem course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_NAME, course.getName());
        contentValues.put(COURSE_COL_PROF, course.getProfessor());
        contentValues.put(COURSE_COL_START, course.getStartTime());
        contentValues.put(COURSE_COL_END, course.getEndTime());
        contentValues.put(COURSE_COL_ROOM, course.getRoom());
        long result = db.insert(COURSE_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertSchedule(ScheduleItem schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COL_USER, schedule.getUser());
        contentValues.put(SCHEDULE_COL_NAME, schedule.getName());
        long result = db.insert(SCHEDULE_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertCourseList(UserItem user, ScheduleItem sched, CourseItem course) {
        SQLiteDatabase db = this.getWritableDatabase();

        int user_id = getUserId(user);
        int schedule_id = getScheduleId(sched);
        int course_id = getCourseId(course);

        long result;
        if (user_id != -1 && schedule_id != -1 && course_id != -1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COURSE_LIST_COL_USER, user_id);
            contentValues.put(COURSE_LIST_COL_SCHED, schedule_id);
            contentValues.put(COURSE_LIST_COL_COURSE, course_id);
            result = db.insert(COURSE_LIST_TABLE_NAME, null, contentValues);
        }
        else
        {
            result = -1;
            Log.e(TAG, "Couldn't find either user, schedule, or course");
        }
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+USER_TABLE_NAME,null);
        return res;
    }

    private int getUserId(UserItem user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int user_id = -1;

        Cursor cursor = db.rawQuery("select * from " + USER_TABLE_NAME +
            " WHERE " + USER_TABLE_NAME + "." + USER_COL_NAME + " =?" ,new String[]{user.getName()});
        if (cursor != null)
        {
            try {
                if (cursor.moveToFirst()) {
                    user_id = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return user_id;
    }

    private int getScheduleId(ScheduleItem schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        int schedule_id = -1;

        Cursor cursor = db.rawQuery("select * from " + SCHEDULE_TABLE_NAME +
            " WHERE " + SCHEDULE_TABLE_NAME + "." + SCHEDULE_COL_NAME + " =?" +
            " AND " + SCHEDULE_TABLE_NAME + "." + SCHEDULE_COL_USER + " =?" , new String[]{schedule.getName(), schedule
            .getUser()});
        if (cursor != null)
        {
            try {
                if (cursor.moveToFirst()) {
                    schedule_id = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return schedule_id;
    }

    private int getCourseId(CourseItem course) {
        SQLiteDatabase db = this.getWritableDatabase();
        int course_id = -1;

        Cursor cursor = db.rawQuery("select * from " + COURSE_TABLE_NAME +
            " WHERE " + COURSE_TABLE_NAME + "." + COURSE_COL_NAME + " =?" +
            " AND " + COURSE_TABLE_NAME + "." + COURSE_COL_PROF + " =?"+
            " AND " + COURSE_TABLE_NAME + "." + COURSE_COL_START + " =?"+
            " AND " + COURSE_TABLE_NAME + "." + COURSE_COL_END + " =?"+
            " AND " + COURSE_TABLE_NAME + "." + COURSE_COL_ROOM + " =?" ,new String[]{
            course.getName(),
            course.getProfessor(),
            course.getStartTime(),
            course.getEndTime(),
            course.getRoom()
        });
        if (cursor != null)
        {
            try {
                if (cursor.moveToFirst()) {
                    course_id = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return course_id;
    }

    private int getCourseListId(UserItem user, ScheduleItem schedule, CourseItem course) {
        int user_id = getUserId(user);
        int schedule_id = getScheduleId(schedule);
        int course_id = getCourseId(course);

        SQLiteDatabase db = this.getWritableDatabase();
        int course_list_id = -1;

        Cursor cursor = db.rawQuery("select * from " + COURSE_LIST_TABLE_NAME +
            " WHERE " + COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_USER + " =?" +
            " AND " + COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_SCHED + " =?"+
            " AND " + COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_COURSE + " =?", new String[]{
            Integer.toString(user_id),
            Integer.toString(schedule_id),
            Integer.toString(course_id)
        });
        if (cursor != null)
        {
            try {
                if (cursor.moveToFirst()) {
                    course_list_id = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return course_list_id;
    }

    public Cursor getSchedules() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+SCHEDULE_TABLE_NAME,null);
        return res;
    }

    public Cursor getSchedulesUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+SCHEDULE_TABLE_NAME +
            " INNER JOIN " + USER_TABLE_NAME + " ON " +
                SCHEDULE_TABLE_NAME + "." + SCHEDULE_COL_USER + " = " + USER_TABLE_NAME + "." + USER_COL_ID +
                " WHERE " + USER_TABLE_NAME + "." + USER_COL_NAME + " = ?" +
                " ORDER BY " + SCHEDULE_TABLE_NAME + "." + SCHEDULE_COL_NAME +
                " LIMIT 0, " + upper_query_limit + " ",
            new String[] {username});
        return res;
    }

    /*
        select course_table.name, course_table.professor, course_table.starttime, course_table.endtime, course_table.room from course_list_table
         INNER JOIN user_table ON course_list_table.user = user_table.ID
         INNER JOIN schedule_table ON course_list_table.schedule = schedule_table.ID
         INNER JOIN course_table ON course_list_table.course = course_table.ID
         WHERE user_table.name = "username" AND schedule_table.name = "schedule"
         ORDER BY course_table.starttime;
         */
    public Cursor getCourseList(String username, String schedulename) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " +
                COURSE_TABLE_NAME + "." + COURSE_COL_NAME + ", " +
                COURSE_TABLE_NAME + "." + COURSE_COL_PROF + ", " +
                COURSE_TABLE_NAME + "." + COURSE_COL_ROOM + ", " +
                COURSE_TABLE_NAME + "." + COURSE_COL_START + ", " +
                COURSE_TABLE_NAME + "." + COURSE_COL_END + " " +
                " FROM " + COURSE_LIST_TABLE_NAME +
                " INNER JOIN " + USER_TABLE_NAME + " ON " +
                    COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_USER + " = " + USER_TABLE_NAME + "." + USER_COL_ID +
                " INNER JOIN " + SCHEDULE_TABLE_NAME + " ON " +
                    COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_SCHED + " = " + SCHEDULE_TABLE_NAME + "." +
                SCHEDULE_COL_ID +
                " INNER JOIN " + COURSE_TABLE_NAME + " ON " +
                    COURSE_LIST_TABLE_NAME + "." + COURSE_LIST_COL_COURSE + " = " + COURSE_TABLE_NAME + "." + COURSE_COL_ID +
                " WHERE " + USER_TABLE_NAME + "." + USER_COL_NAME + " = ?" +
                    "AND " + SCHEDULE_TABLE_NAME + "." + SCHEDULE_COL_NAME + " = ?" +
                " ORDER BY " + COURSE_TABLE_NAME + "." + COURSE_COL_START +
                " LIMIT 0, " + upper_query_limit + " ",
            new String[] {username, schedulename});
        return res;
    }

    public Cursor getCourses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+USER_TABLE_NAME,null);
        return res;
    }

    public boolean updateUser(UserItem user) {
        int user_id = getUserId(user);
        if (user_id == -1) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_NAME, user.getName());
        contentValues.put(USER_COL_FAVSCHED, user.getFavoriteSchedule());
        db.update(USER_TABLE_NAME, contentValues, "ID = ?",new String[] { Integer.toString(user_id) });
        return true;
    }

    public boolean updateCourse(CourseItem course)
    {
        int course_id = getCourseId(course);
        if (course_id == -1) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_NAME, course.getName());
        contentValues.put(COURSE_COL_PROF, course.getProfessor());
        contentValues.put(COURSE_COL_START, course.getStartTime());
        contentValues.put(COURSE_COL_END, course.getEndTime());
        contentValues.put(COURSE_COL_ROOM, course.getRoom());
        db.update(COURSE_TABLE_NAME, contentValues, "ID = ?",new String[] { Integer.toString(course_id) });
        return true;
    }

    public boolean updateSchedule(ScheduleItem schedule) {
        int schedule_id = getScheduleId(schedule);
        if (schedule_id == -1) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COL_USER, schedule.getUser());
        contentValues.put(SCHEDULE_COL_NAME, schedule.getName());
        db.update(SCHEDULE_TABLE_NAME, contentValues, "ID = ?",new String[] { Integer.toString(schedule_id) });
        return true;
    }

    public boolean updateCourseList(UserItem user, ScheduleItem schedule, CourseItem course) {
        int user_id = getUserId(user);
        int schedule_id = getScheduleId(schedule);
        int course_id = getCourseId(course);
        if (user_id == -1 || schedule_id == -1 || course_id == -1) {
            Log.e(TAG, "Couldn't find either user, schedule, or course");
            return false;
        }
        int course_list_id = getCourseListId(user, schedule, course);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_LIST_COL_USER, user_id);
        contentValues.put(COURSE_LIST_COL_SCHED, schedule_id);
        contentValues.put(COURSE_LIST_COL_COURSE, course_id);
        db.update(COURSE_LIST_TABLE_NAME, contentValues, "ID = ?",new String[] { Integer.toString(course_list_id) });
        return true;
    }

    public Integer deleteUser (UserItem user) {
        int user_id = getUserId(user);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME, "ID = ?",new String[] {Integer.toString(user_id)});
    }

    public Integer deleteCourse (CourseItem course) {
        int course_id = getCourseId(course);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COURSE_TABLE_NAME, "ID = ?",new String[] {Integer.toString(course_id)});
    }

    public Integer deleteSchedule (ScheduleItem schedule) {
        int schedule_id = getScheduleId(schedule);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SCHEDULE_TABLE_NAME, "ID = ?",new String[] {Integer.toString(schedule_id)});
    }

    public Integer deleteCourseList (UserItem user, ScheduleItem schedule, CourseItem course) {
        int user_id = getUserId(user);
        int schedule_id = getScheduleId(schedule);
        int course_id = getCourseId(course);
        if (user_id == -1 || schedule_id == -1 || course_id == -1) {
            Log.e(TAG, "Couldn't find either user, schedule, or course");
        }
        int course_list_id = getCourseListId(user, schedule, course);

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COURSE_LIST_TABLE_NAME, "ID = ?",new String[] {Integer.toString(course_list_id)});
    }
}

