package com.lab.service.impl;

import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.LaboratoryMapper;
import com.lab.pojo.Equipment;
import com.lab.pojo.LabEquipmentItem;
import com.lab.pojo.Laboratory;
import com.lab.pojo.PageBean;
import com.lab.service.LaboratoryService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LaboratoryServiceImpl implements LaboratoryService {

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public PageBean<Laboratory> list(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Laboratory> rows = laboratoryMapper.findPage(offset, pageSize);
        Long total = laboratoryMapper.countAll();

        PageBean<Laboratory> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public Laboratory getById(Integer id) {
        return laboratoryMapper.findById(id);
    }

    @Override
    public List<Laboratory> listAvailable() {
        return laboratoryMapper.findAvailableAll();
    }

    @Override
    public List<LabEquipmentItem> getEquipmentByLaboratoryId(Integer laboratoryId) throws JsonProcessingException {
        Laboratory lab = laboratoryMapper.findById(laboratoryId);
        if (lab == null) {
            throw new RuntimeException("实验室不存在");
        }
        String json = lab.getEquipmentList();
        if (json == null || json.trim().isEmpty() || "[]".equals(json.trim())) {
            return new ArrayList<>();
        }
        return new ObjectMapper().readValue(json, new TypeReference<List<LabEquipmentItem>>() {});
    }

    @Override
    public void create(Laboratory laboratory) {
        // 校验管理员权限
        getCurrentAdminId();

        // 检查房间号是否已存在
        if (laboratory.getLabNumber() != null) {
            Laboratory existing = laboratoryMapper.findByLabNumber(laboratory.getLabNumber());
            if (existing != null) {
                throw new RuntimeException("实验室房间号已存在，请使用其他房间号");
            }
        }

        // 设置默认值
        if (laboratory.getCapacity() == null) {
            laboratory.setCapacity(30);
        }

        // 解析前端传入的设备列表，并根据分配数量扣减设备可用数量
        Map<Integer, Integer> newEquipmentMap = parseEquipmentList(laboratory.getEquipmentList());
        int totalCount = 0;
        for (Map.Entry<Integer, Integer> entry : newEquipmentMap.entrySet()) {
            Integer equipmentId = entry.getKey();
            Integer count = entry.getValue();
            if (count == null || count <= 0) {
                continue;
            }
            Equipment equipment = equipmentMapper.findById(equipmentId);
            if (equipment == null) {
                throw new RuntimeException("设备不存在，ID=" + equipmentId);
            }
            Integer available = equipment.getAvailableQuantity();
            if (available == null) {
                available = 0;
            }
            if (count > available) {
                throw new RuntimeException("设备数量不足，设备ID=" + equipmentId);
            }
            int newAvailable = available - count;
            equipmentMapper.updateAvailableQuantity(equipmentId, newAvailable);
            totalCount = totalCount + count;
        }
        laboratory.setEquipmentCount(totalCount);

        // 清理 count 为 0 的设备项，不再保存在 equipmentList 中
        laboratory.setEquipmentList(removeZeroCountItems(laboratory.getEquipmentList()));

        if (laboratory.getStatus() == null) {
            laboratory.setStatus(1); // 默认正常
        }

        laboratoryMapper.add(laboratory);
    }

    @Override
    public void update(Laboratory laboratory) {
        // 校验管理员权限
        getCurrentAdminId();

        // 如果修改了房间号，检查新房间号是否已被其他实验室使用
        Laboratory existing = laboratoryMapper.findById(laboratory.getId());
        if (existing == null) {
            throw new RuntimeException("实验室不存在");
        }

        if (laboratory.getLabNumber() != null && !laboratory.getLabNumber().equals(existing.getLabNumber())) {
            Laboratory existingByLabNumber = laboratoryMapper.findByLabNumber(laboratory.getLabNumber());
            if (existingByLabNumber != null && !existingByLabNumber.getId().equals(laboratory.getId())) {
                throw new RuntimeException("实验室房间号已被其他实验室使用，请使用其他房间号");
            }
        }

        // 设备分配变更时，同步调整各设备的可用数量
        Map<Integer, Integer> oldEquipmentMap = parseEquipmentList(existing.getEquipmentList());
        Map<Integer, Integer> newEquipmentMap = parseEquipmentList(laboratory.getEquipmentList());

        Map<Integer, Boolean> handled = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : oldEquipmentMap.entrySet()) {
            handled.put(entry.getKey(), Boolean.TRUE);
        }
        for (Map.Entry<Integer, Integer> entry : newEquipmentMap.entrySet()) {
            handled.put(entry.getKey(), Boolean.TRUE);
        }

        int totalCount = 0;
        for (Map.Entry<Integer, Boolean> entry : handled.entrySet()) {
            Integer equipmentId = entry.getKey();
            Integer oldCount = oldEquipmentMap.get(equipmentId);
            if (oldCount == null) {
                oldCount = 0;
            }
            Integer newCount = newEquipmentMap.get(equipmentId);
            if (newCount == null) {
                newCount = 0;
            }

            int delta = newCount - oldCount;
            if (delta != 0) {
                Equipment equipment = equipmentMapper.findById(equipmentId);
                if (equipment == null) {
                    throw new RuntimeException("设备不存在，ID=" + equipmentId);
                }
                Integer available = equipment.getAvailableQuantity();
                if (available == null) {
                    available = 0;
                }
                if (delta > 0 && delta > available) {
                    throw new RuntimeException("设备数量不足，设备ID=" + equipmentId);
                }
                int newAvailable = available - delta;
                equipmentMapper.updateAvailableQuantity(equipmentId, newAvailable);
            }

            if (newCount > 0) {
                totalCount = totalCount + newCount;
            }
        }
        laboratory.setEquipmentCount(totalCount);

        // 清理 count 为 0 的设备项，不再保存在 equipmentList 中
        laboratory.setEquipmentList(removeZeroCountItems(laboratory.getEquipmentList()));

        laboratoryMapper.update(laboratory);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();

        // 删除实验室
        laboratoryMapper.delete(id);
    }

    @Override
    @Transactional
    public void addEquipment(Integer laboratoryId, Integer equipmentId, Integer quantity) {
        // 校验管理员权限
        getCurrentAdminId();

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("设备数量必须大于0");
        }

        // 检查实验室是否存在
        Laboratory laboratory = laboratoryMapper.findById(laboratoryId);
        if (laboratory == null) {
            throw new RuntimeException("实验室不存在");
        }

        // 检查设备是否存在
        Equipment equipment = equipmentMapper.findById(equipmentId);
        if (equipment == null) {
            throw new RuntimeException("设备不存在");
        }

        Integer available = equipment.getAvailableQuantity();
        if (available == null) {
            available = 0;
        }
        if (quantity > available) {
            throw new RuntimeException("设备数量不足");
        }

        String equipmentListJson = laboratory.getEquipmentList();
        String equipmentName = equipment.getEquipmentName();

        if (equipmentListJson == null || equipmentListJson.trim().isEmpty()) {
            equipmentListJson = "[{\"id\":" + equipmentId + ",\"name\":\"" + equipmentName + "\",\"count\":" + quantity + "}]";
        } else {
            String idPattern = "\"id\":" + equipmentId + ",";
            int index = equipmentListJson.indexOf(idPattern);
            if (index == -1) {
                String newItem = "{\"id\":" + equipmentId + ",\"name\":\"" + equipmentName + "\",\"count\":" + quantity + "}";
                if (equipmentListJson.equals("[]")) {
                    equipmentListJson = "[" + newItem + "]";
                } else if (equipmentListJson.endsWith("]")) {
                    equipmentListJson = equipmentListJson.substring(0, equipmentListJson.length() - 1)
                            + "," + newItem + "]";
                } else {
                    equipmentListJson = "[" + newItem + "]";
                }
            } else {
                String countKey = "\"count\":";
                int countIndex = equipmentListJson.indexOf(countKey, index);
                if (countIndex == -1) {
                    String newItem = "{\"id\":" + equipmentId + ",\"name\":\"" + equipmentName + "\",\"count\":" + quantity + "}";
                    equipmentListJson = "[" + newItem + "]";
                } else {
                    int valueStart = countIndex + countKey.length();
                    int valueEnd = valueStart;
                    while (valueEnd < equipmentListJson.length()
                            && Character.isDigit(equipmentListJson.charAt(valueEnd))) {
                        valueEnd++;
                    }
                    int oldCount = Integer.parseInt(equipmentListJson.substring(valueStart, valueEnd));
                    int newCount = oldCount + quantity;
                    equipmentListJson = equipmentListJson.substring(0, valueStart)
                            + newCount
                            + equipmentListJson.substring(valueEnd);
                }
            }
        }

        // 重新统计实验室内设备总数
        int totalCount = 0;
        String search = "\"count\":";
        int searchIndex = equipmentListJson.indexOf(search);
        while (searchIndex != -1) {
            int valueStart = searchIndex + search.length();
            int valueEnd = valueStart;
            while (valueEnd < equipmentListJson.length()
                    && Character.isDigit(equipmentListJson.charAt(valueEnd))) {
                valueEnd++;
            }
            totalCount = totalCount + Integer.parseInt(equipmentListJson.substring(valueStart, valueEnd));
            searchIndex = equipmentListJson.indexOf(search, valueEnd);
        }

        laboratory.setEquipmentList(equipmentListJson);
        laboratory.setEquipmentCount(totalCount);
        laboratoryMapper.update(laboratory);

        // 更新设备可用数量
        Integer newAvailable = available - quantity;
        equipmentMapper.updateAvailableQuantity(equipmentId, newAvailable);
    }

    /**
     * 从 ThreadLocal（token解析后的claims）中直接获取管理员ID
     */
    private Integer getCurrentAdminId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer adminId = (Integer) claims.get("id");
        if (adminId == null) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        return adminId;
    }

    /**
     * 解析实验室中 equipmentList 字段（JSON 字符串），提取每个设备的 id 和 count
     * 返回 Map<设备ID, 数量>
     */
    private Map<Integer, Integer> parseEquipmentList(String equipmentListJson) {
        Map<Integer, Integer> result = new HashMap<>();
        if (equipmentListJson == null) {
            return result;
        }
        String json = equipmentListJson.trim();
        if (json.isEmpty() || "[]".equals(json)) {
            return result;
        }

        String idKey = "\"id\":";
        String countKey = "\"count\":";
        int searchIndex = json.indexOf(idKey);
        while (searchIndex != -1) {
            int idStart = searchIndex + idKey.length();
            while (idStart < json.length() && (json.charAt(idStart) == ' ' || json.charAt(idStart) == '\n' || json.charAt(idStart) == '\r')) {
                idStart++;
            }
            int idEnd = idStart;
            while (idEnd < json.length() && Character.isDigit(json.charAt(idEnd))) {
                idEnd++;
            }
            if (idEnd == idStart) {
                searchIndex = json.indexOf(idKey, idStart);
                continue;
            }
            Integer id = Integer.parseInt(json.substring(idStart, idEnd));

            int countIndex = json.indexOf(countKey, idEnd);
            if (countIndex == -1) {
                searchIndex = json.indexOf(idKey, idEnd);
                continue;
            }
            int valueStart = countIndex + countKey.length();
            while (valueStart < json.length() && (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '\n' || json.charAt(valueStart) == '\r')) {
                valueStart++;
            }
            int valueEnd = valueStart;
            while (valueEnd < json.length() && Character.isDigit(json.charAt(valueEnd))) {
                valueEnd++;
            }
            if (valueEnd == valueStart) {
                searchIndex = json.indexOf(idKey, valueStart);
                continue;
            }
            Integer count = Integer.parseInt(json.substring(valueStart, valueEnd));
            result.put(id, count);

            searchIndex = json.indexOf(idKey, valueEnd);
        }

        return result;
    }

    /**
     * 移除 equipmentList JSON 中 count 为 0 的设备项
     * 不依赖 JSON 库，按字符串简单解析
     */
    private String removeZeroCountItems(String equipmentListJson) {
        if (equipmentListJson == null) {
            return null;
        }
        String json = equipmentListJson.trim();
        if (json.isEmpty() || "[]".equals(json)) {
            return json;
        }
        if (!json.startsWith("[")) {
            return json;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        int len = json.length();
        int index = 0;
        while (true) {
            int objStart = json.indexOf('{', index);
            if (objStart == -1) {
                break;
            }
            int brace = 1;
            int i = objStart + 1;
            while (i < len && brace > 0) {
                char c = json.charAt(i);
                if (c == '{') {
                    brace++;
                } else if (c == '}') {
                    brace--;
                }
                i++;
            }
            int objEnd = i;
            if (objEnd <= objStart) {
                break;
            }
            String obj = json.substring(objStart, objEnd);

            if (!isZeroCountObject(obj)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(obj);
                first = false;
            }

            index = objEnd;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 判断一个对象字符串中 count 是否为 0
     */
    private boolean isZeroCountObject(String obj) {
        if (obj == null) {
            return false;
        }
        String key = "\"count\"";
        int idx = obj.indexOf(key);
        if (idx == -1) {
            return false;
        }
        int colon = obj.indexOf(":", idx + key.length());
        if (colon == -1) {
            return false;
        }
        int i = colon + 1;
        int len = obj.length();
        while (i < len) {
            char c = obj.charAt(i);
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                i++;
            } else {
                break;
            }
        }
        if (i >= len) {
            return false;
        }
        char first = obj.charAt(i);
        if (first != '0') {
            return false;
        }
        // 如果第一位是 0，且下一位不是数字，则认为是 0
        if (i + 1 >= len) {
            return true;
        }
        char next = obj.charAt(i + 1);
        if (next < '0' || next > '9') {
            return true;
        }
        return false;
    }
}

