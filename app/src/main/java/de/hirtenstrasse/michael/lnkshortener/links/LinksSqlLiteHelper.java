package de.hirtenstrasse.michael.lnkshortener.links;

// Copyright (C) 2017 Michael Achmann

//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LinksSqlLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_LINKS = "links";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHORT_LINK = "short_link";
    public static final String COLUMN_LONG_LINK = "long_link";
    public static final String COLUMN_ADDED = "added";
    public static final String COLUMN_LINK_TITLE = "link_title";
    public static final String COLUMN_LINK_IMAGE = "link_image";
    public static final String COLUMN_STARRED = "starred";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CLICKS = "clicks";
    public static final String COLUMN_LAST_UPDATED = "last_updated";
    public static final String COLUMN_SECRET = "secret";

    private static final String DATABASE_NAME = "links.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
                                            + TABLE_LINKS + "( " + COLUMN_ID
                                            + "integer primary key autoincrement, "
                                            + COLUMN_LONG_LINK + " text not null "
                                            + COLUMN_SHORT_LINK + " text not null "
                                            + COLUMN_ADDED + " integer not null default current_timestamp"
                                            + COLUMN_LINK_TITLE + " text no null "
                                            + COLUMN_LINK_IMAGE + " blob "
                                            + COLUMN_STARRED + " integer "
                                            + COLUMN_CATEGORY + " integer "
                                            + COLUMN_CLICKS + " integer "
                                            + COLUMN_LAST_UPDATED + " not null default current_timestamp "
                                            + COLUMN_SECRET + " integer "
                                            + ");";

    public LinksSqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKS);
        onCreate(db);
    }
}
