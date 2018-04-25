package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;
import demo.spring.boot.demospringboot.sdxd.common.pojo.dto.PaginationSupport;


import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Throwables.propagate;

public class Pages<T> implements Iterable<T>, Iterator<T> {

    private int currentPage = 0;
    private Stack<T> records = new Stack<>();
    private int pageSize;
    private boolean hasMorePage = true;

    private int currentIndex;

    private BiFunctionE<Integer, Integer, PaginationSupport<T>, ProcessBizException> function;

    public Pages(int pageSize, BiFunctionE<Integer, Integer, PaginationSupport<T>, ProcessBizException> function) {
        this.pageSize = pageSize;
        this.function = function;

        this.currentIndex = 0;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public PaginationSupport<T> specified(int pageNo, int pageSize) throws ProcessBizException {
        return specifiedPage(pageNo, pageSize);
    }

    @Override
    public boolean hasNext() {
        boolean emptyStack = records.size() == 0;
        if (emptyStack && hasMorePage) {
            PaginationSupport<T> page = nextPage();
            Collection<T> results = page == null ? null : page.getList();
            hasMorePage = results != null && results.size() > pageSize;
            if (results != null && results.size() > 0) {
                records.addAll(results);
            }
        }
        return records.size() > 0 || hasMorePage;
    }

    @Override
    public T next() {
        T t = records.pop();
        this.currentIndex ++;
        return t;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    private PaginationSupport<T> nextPage() {
        return propagate(() -> function.apply(++ currentPage, pageSize + 1));
    }

    private PaginationSupport<T> specifiedPage(int pageNo, int pageSize) throws ProcessBizException {
        return function.apply(pageNo, pageSize);
    }
}
