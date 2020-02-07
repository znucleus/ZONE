package top.zbeboy.zone.service.plugin;

import org.apache.commons.lang3.BooleanUtils;
import org.jooq.*;
import top.zbeboy.zone.web.util.pagination.PaginationUtil;

import java.util.List;
import java.util.Objects;

public interface PaginationPlugin<S extends PaginationUtil> {

    /**
     * 分页查询
     *
     * @param create         当前查询器
     * @param table          表
     * @param paginationUtil 分页工具
     * @return 分页数据
     */
    default Result<Record> queryAllByPage(final DSLContext create, TableLike<?> table, S paginationUtil, boolean useExtraCondition) {
        Result<Record> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            SelectJoinStep<Record> selectJoinStep = create.select()
                    .from(table);
            sortCondition(selectJoinStep, paginationUtil);
            pagination(selectJoinStep, paginationUtil);
            records = selectJoinStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(a);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 多表查询
     *
     * @param selectOnConditionStep 关联表
     * @param paginationUtil        分页工具
     * @param useExtraCondition     是否使用额外条件
     * @return 分页数据
     */
    default Result<Record> queryAllByPage(SelectOnConditionStep<Record> selectOnConditionStep, S paginationUtil, boolean useExtraCondition) {
        Result<Record> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            sortCondition(selectOnConditionStep, paginationUtil);
            pagination(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(a);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 多表查询，自定义构建数据
     *
     * @param selectOnConditionStep 关联表
     * @param paginationUtil        分页工具
     * @param useExtraCondition     是否使用额外条件
     */
    default List<?> queryAllByPageAndBuildData(SelectOnConditionStep<Record> selectOnConditionStep, S paginationUtil, boolean useExtraCondition) {
        return buildData(queryAllByPage(selectOnConditionStep, paginationUtil, useExtraCondition));
    }

    /**
     * 根据额外自定义条件分页查询
     *
     * @param create         当前查询器
     * @param table          表
     * @param extraCondition 额外条件
     * @param paginationUtil 分页工具类
     * @return 分页数据
     */
    default Result<Record> queryAllByPage(final DSLContext create, TableLike<?> table, Condition extraCondition, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(extraCondition);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(a.and(extraCondition));
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 多表额外条件查询
     *
     * @param selectOnConditionStep 关联表
     * @param extraCondition        额外条件
     * @param paginationUtil        分页工具
     * @return 分页数据
     */
    default Result<Record> queryAllByPage(SelectOnConditionStep<Record> selectOnConditionStep, Condition extraCondition, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(extraCondition);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(a.and(extraCondition));
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 分页带group by
     *
     * @param selectOnConditionStep 关联表
     * @param paginationUtil        分页工具
     * @param useExtraCondition     使用额外条件
     * @param groupFields           group by 变量
     * @return 数据
     */
    default Result<Record> queryAllByPageWithGroupBy(SelectOnConditionStep<Record> selectOnConditionStep, S paginationUtil, boolean useExtraCondition, GroupField... groupFields) {
        Result<Record> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            groupByCondition(selectOnConditionStep, groupFields);
            sortCondition(selectOnConditionStep, paginationUtil);
            pagination(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(a);
            groupByCondition(selectConditionStep, groupFields);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 查询全部数据
     *
     * @param create         当前查询器
     * @param table          表
     * @param paginationUtil 工具类
     * @return 全部数据
     */
    default Result<Record> queryAll(final DSLContext create, TableLike<?> table, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectJoinStep<Record> selectJoinStep = create.select()
                    .from(table);
            sortCondition(selectJoinStep, paginationUtil);
            records = selectJoinStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(a);
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 多表查询全部
     *
     * @param selectOnConditionStep 关联表
     * @param paginationUtil        分页工具
     * @return 全部数据
     */
    default Result<Record> queryAll(SelectOnConditionStep<Record> selectOnConditionStep, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            sortCondition(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(a);
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 根据额外条件查询全部数据
     *
     * @param create         当前查询器
     * @param table          表
     * @param extraCondition 额外条件
     * @param paginationUtil 工具类
     * @return 全部数据
     */
    default Result<Record> queryAll(final DSLContext create, TableLike<?> table, Condition extraCondition, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(extraCondition);
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = create.select()
                    .from(table)
                    .where(a.and(extraCondition));
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 多表查询全部数据
     *
     * @param selectOnConditionStep 关联表
     * @param extraCondition        额外条件
     * @param paginationUtil        分页工具
     * @return 全部数据
     */
    default Result<Record> queryAll(SelectOnConditionStep<Record> selectOnConditionStep, Condition extraCondition, S paginationUtil) {
        Result<Record> records;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(extraCondition);
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        } else {
            SelectConditionStep<Record> selectConditionStep = selectOnConditionStep.where(a.and(extraCondition));
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    /**
     * 统计数量
     *
     * @param create 当前查询器
     * @param table  表
     * @return 统计结果
     */
    default int countAll(final DSLContext create, TableLike<?> table) {
        Record1<Integer> count = create.selectCount()
                .from(table)
                .fetchOne();
        return count.value1();
    }

    /**
     * 多表统计
     *
     * @param selectOnConditionStep 关联表
     * @return 结果
     */
    default int countAll(SelectOnConditionStep<Record1<Integer>> selectOnConditionStep) {
        Record1<Integer> count = selectOnConditionStep
                .fetchOne();
        return count.value1();
    }

    /**
     * 根据额外条件统计
     *
     * @param create         当前查询器
     * @param table          表
     * @param extraCondition 额外条件
     * @return 统计结果
     */
    default int countAll(final DSLContext create, TableLike<?> table, Condition extraCondition) {
        Record1<Integer> count = create.selectCount()
                .from(table)
                .where(extraCondition)
                .fetchOne();
        return count.value1();
    }

    /**
     * 多表根据额外条件统计
     *
     * @param selectOnConditionStep 关联表
     * @param extraCondition        额外条件
     * @return 统计结果
     */
    default int countAll(SelectOnConditionStep<Record1<Integer>> selectOnConditionStep, Condition extraCondition) {
        Record1<Integer> count = selectOnConditionStep
                .where(extraCondition)
                .fetchOne();
        return count.value1();
    }

    /**
     * 根据搜索条件统计
     *
     * @param create            当前查询器
     * @param table             表
     * @param paginationUtil    分页工具类
     * @param useExtraCondition 是否使用固定额外条件
     * @return 统计结果
     */
    default int countAll(final DSLContext create, TableLike<?> table, S paginationUtil, boolean useExtraCondition) {
        Record1<Integer> count;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            SelectJoinStep<Record1<Integer>> selectJoinStep = create.selectCount()
                    .from(table);
            count = selectJoinStep.fetchOne();
        } else {
            SelectConditionStep<Record1<Integer>> selectConditionStep = create.selectCount()
                    .from(table)
                    .where(a);
            count = selectConditionStep.fetchOne();
        }
        return count.value1();
    }

    /**
     * 多表根据搜索条件统计
     *
     * @param selectOnConditionStep 关联表
     * @param paginationUtil        分页工具
     * @param useExtraCondition     是否使用固定额外条件
     * @return 统计结果
     */
    default int countAll(SelectOnConditionStep<Record1<Integer>> selectOnConditionStep, S paginationUtil, boolean useExtraCondition) {
        Record1<Integer> count;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            count = selectOnConditionStep.fetchOne();
        } else {
            SelectConditionStep<Record1<Integer>> selectConditionStep = selectOnConditionStep
                    .where(a);
            count = selectConditionStep.fetchOne();
        }
        return count.value1();
    }

    /**
     * 根据搜索条件以及额外条件统计
     *
     * @param create         当前查询器
     * @param table          表
     * @param extraCondition 额外条件
     * @param paginationUtil 分页工具
     * @return 统计结果
     */
    default int countAll(final DSLContext create, TableLike<?> table, Condition extraCondition, S paginationUtil) {
        Record1<Integer> count;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record1<Integer>> selectConditionStep = create.selectCount()
                    .from(table)
                    .where(extraCondition);
            count = selectConditionStep.fetchOne();
        } else {
            SelectConditionStep<Record1<Integer>> selectConditionStep = create.selectCount()
                    .from(table)
                    .where(a.and(extraCondition));
            count = selectConditionStep.fetchOne();
        }
        return count.value1();
    }

    /**
     * 多表根据搜索条件以及额外条件统计
     *
     * @param selectOnConditionStep 关联表
     * @param extraCondition        额外条件
     * @param paginationUtil        分页工具
     * @return 统计结果
     */
    default int countAll(SelectOnConditionStep<Record1<Integer>> selectOnConditionStep, Condition extraCondition, S paginationUtil) {
        Record1<Integer> count;
        Condition a = searchCondition(paginationUtil);
        if (Objects.isNull(a)) {
            SelectConditionStep<Record1<Integer>> selectConditionStep = selectOnConditionStep
                    .where(extraCondition);
            count = selectConditionStep.fetchOne();
        } else {
            SelectConditionStep<Record1<Integer>> selectConditionStep = selectOnConditionStep
                    .where(a.and(extraCondition));
            count = selectConditionStep.fetchOne();
        }
        return count.value1();
    }

    /**
     * 搜索条件
     *
     * @param paginationUtil 分页工具类
     * @return 条件
     */
    default Condition searchCondition(S paginationUtil) {
        return null;
    }

    /**
     * 额外固定条件
     *
     * @param paginationUtil 分页工具类
     * @return 额外固定条件
     */
    default Condition extraCondition(S paginationUtil) {
        return null;
    }

    /**
     * 条件合并
     *
     * @param paginationUtil    分页工具类
     * @param useExtraCondition 优先使用额外条件
     * @return 各条件合并
     */
    default Condition useExtraCondition(S paginationUtil, boolean useExtraCondition) {
        Condition a;
        if (BooleanUtils.isFalse(useExtraCondition)) {
            a = searchCondition(paginationUtil);
            if (Objects.nonNull(a)) {
                Condition b = extraCondition(paginationUtil);
                if (Objects.nonNull(b)) {
                    a = a.and(b);
                }
            } else {
                a = extraCondition(paginationUtil);
            }
        } else {
            a = extraCondition(paginationUtil);
        }
        return a;
    }

    /**
     * 排序
     *
     * @param step           结果条件
     * @param paginationUtil 分页工具类
     */
    default void sortCondition(SelectConnectByStep<Record> step, S paginationUtil) {
    }

    /**
     * 排序完成
     *
     * @param step      结果条件
     * @param sortField 排序条件
     */
    default void sortToFinish(SelectConnectByStep<Record> step, SortField... sortField) {
        if (Objects.nonNull(sortField)) {
            step.orderBy(sortField);
        }
    }

    /**
     * 排序
     *
     * @param step        结果条件
     * @param groupFields 合并条件
     */
    default void groupByCondition(SelectConnectByStep<Record> step, GroupField... groupFields) {
        if (Objects.nonNull(groupFields)) {
            step.groupBy(groupFields);
        }
    }

    /**
     * 分页
     *
     * @param step           结果条件
     * @param paginationUtil 分页工具类
     */
    default void pagination(SelectConnectByStep<Record> step, S paginationUtil) {
        int start = paginationUtil.getStart();
        int length = paginationUtil.getLength();

        step.limit(start, length);
    }

    /**
     * 构建自定义数据
     *
     * @param records 待转换数据
     */
    default List<?> buildData(Result<Record> records) {
        return null;
    }
}
