package com.repforge.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.repforge.app.data.entities.ExerciseLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutDao_Impl implements WorkoutDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseLog> __insertionAdapterOfExerciseLog;

  public WorkoutDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseLog = new EntityInsertionAdapter<ExerciseLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exercise_logs` (`id`,`dateMillis`,`workoutType`,`exerciseName`,`weightKg`,`repsAchieved`,`targetReps`,`allRepsHit`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDateMillis());
        statement.bindString(3, entity.getWorkoutType());
        statement.bindString(4, entity.getExerciseName());
        statement.bindDouble(5, entity.getWeightKg());
        statement.bindString(6, entity.getRepsAchieved());
        statement.bindLong(7, entity.getTargetReps());
        final int _tmp = entity.getAllRepsHit() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
  }

  @Override
  public Object insertExerciseLog(final ExerciseLog log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastPerformance(final String name,
      final Continuation<? super ExerciseLog> $completion) {
    final String _sql = "SELECT * FROM exercise_logs WHERE exerciseName = ? ORDER BY dateMillis DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, name);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseLog>() {
      @Override
      @Nullable
      public ExerciseLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dateMillis");
          final int _cursorIndexOfWorkoutType = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutType");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfRepsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "repsAchieved");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfAllRepsHit = CursorUtil.getColumnIndexOrThrow(_cursor, "allRepsHit");
          final ExerciseLog _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateMillis;
            _tmpDateMillis = _cursor.getLong(_cursorIndexOfDateMillis);
            final String _tmpWorkoutType;
            _tmpWorkoutType = _cursor.getString(_cursorIndexOfWorkoutType);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final String _tmpRepsAchieved;
            _tmpRepsAchieved = _cursor.getString(_cursorIndexOfRepsAchieved);
            final int _tmpTargetReps;
            _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
            final boolean _tmpAllRepsHit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllRepsHit);
            _tmpAllRepsHit = _tmp != 0;
            _result = new ExerciseLog(_tmpId,_tmpDateMillis,_tmpWorkoutType,_tmpExerciseName,_tmpWeightKg,_tmpRepsAchieved,_tmpTargetReps,_tmpAllRepsHit);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentLogs(final long sinceMillis,
      final Continuation<? super List<ExerciseLog>> $completion) {
    final String _sql = "SELECT * FROM exercise_logs WHERE dateMillis >= ? ORDER BY dateMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sinceMillis);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseLog>>() {
      @Override
      @NonNull
      public List<ExerciseLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dateMillis");
          final int _cursorIndexOfWorkoutType = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutType");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfRepsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "repsAchieved");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfAllRepsHit = CursorUtil.getColumnIndexOrThrow(_cursor, "allRepsHit");
          final List<ExerciseLog> _result = new ArrayList<ExerciseLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateMillis;
            _tmpDateMillis = _cursor.getLong(_cursorIndexOfDateMillis);
            final String _tmpWorkoutType;
            _tmpWorkoutType = _cursor.getString(_cursorIndexOfWorkoutType);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final String _tmpRepsAchieved;
            _tmpRepsAchieved = _cursor.getString(_cursorIndexOfRepsAchieved);
            final int _tmpTargetReps;
            _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
            final boolean _tmpAllRepsHit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllRepsHit);
            _tmpAllRepsHit = _tmp != 0;
            _item = new ExerciseLog(_tmpId,_tmpDateMillis,_tmpWorkoutType,_tmpExerciseName,_tmpWeightKg,_tmpRepsAchieved,_tmpTargetReps,_tmpAllRepsHit);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastWorkout(final Continuation<? super ExerciseLog> $completion) {
    final String _sql = "SELECT * FROM exercise_logs ORDER BY dateMillis DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseLog>() {
      @Override
      @Nullable
      public ExerciseLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dateMillis");
          final int _cursorIndexOfWorkoutType = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutType");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfRepsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "repsAchieved");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfAllRepsHit = CursorUtil.getColumnIndexOrThrow(_cursor, "allRepsHit");
          final ExerciseLog _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateMillis;
            _tmpDateMillis = _cursor.getLong(_cursorIndexOfDateMillis);
            final String _tmpWorkoutType;
            _tmpWorkoutType = _cursor.getString(_cursorIndexOfWorkoutType);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final String _tmpRepsAchieved;
            _tmpRepsAchieved = _cursor.getString(_cursorIndexOfRepsAchieved);
            final int _tmpTargetReps;
            _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
            final boolean _tmpAllRepsHit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllRepsHit);
            _tmpAllRepsHit = _tmp != 0;
            _result = new ExerciseLog(_tmpId,_tmpDateMillis,_tmpWorkoutType,_tmpExerciseName,_tmpWeightKg,_tmpRepsAchieved,_tmpTargetReps,_tmpAllRepsHit);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
