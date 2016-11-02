package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.service.PluginService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {
    @Resource
    private List<PaymentPlugin> paymentPlugins = new ArrayList();
    @Resource
    private List<StoragePlugin> storagePlugins = new ArrayList();
    @Resource
    private Map<String, PaymentPlugin> paymentPluginMap = new HashMap();
    @Resource
    private Map<String, StoragePlugin> storagePluginMap = new HashMap();

    public List<PaymentPlugin> getPaymentPlugins() {
        Collections.sort(this.paymentPlugins);
        return this.paymentPlugins;
    }

    public List<StoragePlugin> getStoragePlugins() {
        Collections.sort(this.storagePlugins);
        return this.storagePlugins;
    }

    public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled) {
        ArrayList list = new ArrayList();
        CollectionUtils.select(this.paymentPlugins, new _cls1(isEnabled), list);
        Collections.sort(list);
        return list;
    }

    public List<StoragePlugin> getStoragePlugins(boolean isEnabled) {
        ArrayList localArrayList = new ArrayList();
        CollectionUtils.select(this.storagePlugins, new _cls2(isEnabled), localArrayList);
        Collections.sort(localArrayList);
        return localArrayList;
    }

    public PaymentPlugin getPaymentPlugin(String id) {
        return (PaymentPlugin) this.paymentPluginMap.get(id);
    }

    public StoragePlugin getStoragePlugin(String id) {
        return (StoragePlugin) this.storagePluginMap.get(id);
    }

    private class _cls1 implements Predicate {

        final PluginServiceImpl pluginService;
        private final boolean flag;

        _cls1(boolean flag) {
            super();
            pluginService = PluginServiceImpl.this;
            this.flag = flag;

        }

        public boolean evaluate(Object object) {
            PaymentPlugin paymentplugin = (PaymentPlugin) object;
            return paymentplugin.getIsEnabled() == flag;
        }

    }

    private class _cls2 implements Predicate {

        public boolean evaluate(Object object) {
            StoragePlugin storageplugin = (StoragePlugin) object;
            return storageplugin.getIsEnabled() == flag;
        }

        final PluginServiceImpl pluginService;
        private final boolean flag;

        _cls2(boolean flag) {
            super();
            pluginService = PluginServiceImpl.this;
            this.flag = flag;

        }
    }

}