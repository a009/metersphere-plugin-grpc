package io.metersphere.plugin.grpc;

import io.metersphere.plugin.core.api.UiScriptApi;
import io.metersphere.plugin.core.ui.PluginResource;
import io.metersphere.plugin.core.ui.UiScript;
import io.metersphere.plugin.core.utils.LogUtil;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class UiScriptApiImpl extends UiScriptApi {
    /**
     * 企业版插件增加 这个方法
     *
     * @return 是否是企业版插件
     */
    public boolean xpack() {
        return false;
    }

    @Override
    public PluginResource init() {
        LogUtil.info("开始初始化 grpcSampler 插件脚本内容 ");
        List<UiScript> uiScripts = new LinkedList<>();

        // 获取表单 json
        String grpcSampler = getJson("/json/ui_grpc.json");

        // 每个 UiScript 对象对应接口自动化中的一个步骤
        UiScript grpcScript = new UiScript("grpc_sampler", "GRPC Request", "io.metersphere.plugin.grpc.sampler.MsGrpcSampler", grpcSampler);

        // 指定对应的 JMeter 基类，用于进行是否可添加到
        grpcScript.setJmeterClazz("AbstractSampler");

        // 指定表单配置
        grpcScript.setFormOption(getJson("/json/ui_form.json"));

        // 一个插件提供多个步骤时，构造并添加多个 UiScript 对象
        uiScripts.add(grpcScript);
        LogUtil.info("初始化 grpcSampler 插件脚本内容结束 ");
        return new PluginResource("grpc-v1.0.0", uiScripts);
    }

    public String getJson(String path) {
        try {
            InputStream in = UiScriptApiImpl.class.getResourceAsStream(path);
            String json = org.apache.commons.io.IOUtils.toString(in);
            return json;
        } catch (Exception ex) {
            LogUtil.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public String customMethod(String req) {
        LogUtil.info("执行 MsGrpcSampler 插件中的自定义方法");
        return null;
    }
}
