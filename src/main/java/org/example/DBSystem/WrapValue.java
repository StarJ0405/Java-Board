package org.example.DBSystem;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class WrapValue {
    protected final int[] nums;

    public WrapValue(int... num) {
        this.nums = num;
    }

    public abstract void apply(PreparedStatement preparedStatement) throws SQLException;

    private static class WrapString extends WrapValue {
        protected final String value;

        private WrapString(String value, int... nums) {
            super(nums);
            this.value = value;
        }

        @Override
        public void apply(PreparedStatement preparedStatement) throws SQLException {
            for (int num : nums)
                preparedStatement.setString(num, value);
        }
    }

    private static class WrapInt extends WrapValue {
        protected final int value;

        private WrapInt(int value, int... nums) {
            super(nums);
            this.value = value;
        }

        @Override
        public void apply(PreparedStatement preparedStatement) throws SQLException {
            for (int num : nums)
                preparedStatement.setInt(num, value);
        }
    }

    public static WrapString getString(String value, int... nums) {
        return new WrapString(value, nums);
    }

    public static WrapInt getInt(boolean value, int... nums) {
        return new WrapInt(value ? 1 : 0, nums);
    }

    public static WrapInt getInt(int value, int... nums) {
        return new WrapInt(value, nums);
    }
}
