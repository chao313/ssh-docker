package demo.spring.boot.demospringboot.sdxd.common.api.common.web.monitor;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessEvent;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.access.AccessListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/7/11     melvin                 Created
 */
public class TraceListener implements AccessListener {

    private static final Logger log = LoggerFactory.getLogger(TraceListener.class);

    private TraceRepository repository;

    public TraceListener(TraceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onEvent(AccessEvent event) {
        if (!repository.isValid(event)) {
            return;
        }

        Observable.fromCallable(() -> {
            repository.cache(event);
            return null;
        }).subscribeOn(Schedulers.io()).subscribe(o -> {
        }, throwable -> log.error("Count api access failed.", throwable));
    }




}
