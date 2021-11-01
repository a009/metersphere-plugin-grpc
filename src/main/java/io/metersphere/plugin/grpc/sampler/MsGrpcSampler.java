package io.metersphere.plugin.grpc.sampler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import io.metersphere.plugin.core.MsParameter;
import io.metersphere.plugin.core.MsTestElement;
import io.metersphere.plugin.core.utils.LogUtil;
import io.metersphere.plugin.grpc.utils.ElementUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import vn.zalopay.benchmark.GRPCSampler;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MsGrpcSampler extends MsTestElement {
    public MsGrpcSampler() {
    }

    private String clazzName = "io.metersphere.plugin.grpc.sampler.MsGrpcSampler";

    @JSONField(ordinal = 10)
    private String protoFolder;
    @JSONField(ordinal = 11)
    private String libFolder;
    @JSONField(ordinal = 12)
    private String metadata;
    @JSONField(ordinal = 13)
    private String host;
    @JSONField(ordinal = 14)
    private String port;
    @JSONField(ordinal = 15)
    private String fullMethod;
    @JSONField(ordinal = 16)
    private String deadline;
    @JSONField(ordinal = 17)
    private Boolean tls;
    @JSONField(ordinal = 18)
    private String requestJson;

    @Override
    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, MsParameter config) {
        LogUtil.info("===========开始转换MsGrpcSampler ==================");
        if (!this.isEnable()) {
            return;
        }
        GRPCSampler initGrpcSampler = initGrpcSampler();
        if (initGrpcSampler != null) {
            tree.add(initGrpcSampler());
            final HashTree grpcTree = tree;
            if (CollectionUtils.isNotEmpty(hashTree)) {
                for (MsTestElement el : hashTree) {
                    el.toHashTree(grpcTree, el.getHashTree(), config);
                }
            }
        } else {
            LogUtil.error("GRPC Sampler 生成失败");
            throw new RuntimeException("GRPC Sampler生成失败");
        }
    }

    public GRPCSampler initGrpcSampler() {
        try {
            GRPCSampler grpcSampler = new GRPCSampler();
            // base 执行时需要的参数, MsElement 的通用属性
            grpcSampler.setProperty("MS-ID", this.getId());
            String indexPath = this.getIndex();
            grpcSampler.setProperty("MS-RESOURCE-ID", this.getResourceId() + "_" + ElementUtil.getFullIndexPath(this.getParent(), indexPath));
            List<String> id_names = new LinkedList<>();
            ElementUtil.getScenarioSet(this, id_names);
            grpcSampler.setProperty("MS-SCENARIO", JSON.toJSONString(id_names));
            grpcSampler.setEnabled(this.isEnable());
            grpcSampler.setName(this.getName());

            // jmx 中 GrpcSampler 对应的 guiclass 和 testclass 属性
            grpcSampler.setProperty(TestElement.GUI_CLASS, "vn.zalopay.benchmark.GRPCSamplerGui");
            grpcSampler.setProperty(TestElement.TEST_CLASS, "vn.zalopay.benchmark.GRPCSampler");

            // 为 jmx 中 GrpcSampler 所需的各个属性赋值
            grpcSampler.setProperty("GRPCSampler.protoFolder", this.getProtoFolder());
            grpcSampler.setProperty("GRPCSampler.libFolder", this.getLibFolder());
            grpcSampler.setProperty("GRPCSampler.metadata", this.getMetadata());
            grpcSampler.setProperty("GRPCSampler.host", this.getHost());
            grpcSampler.setProperty("GRPCSampler.port", this.getPort());
            grpcSampler.setProperty("GRPCSampler.fullMethod", this.getFullMethod());
            grpcSampler.setProperty("GRPCSampler.deadline", this.getDeadline());
            grpcSampler.setProperty("GRPCSampler.tls", this.getTls());
            grpcSampler.setProperty("GRPCSampler.requestJson", this.getRequestJson());

            return grpcSampler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
