create database farmsearch;

-- 节点企业信息表
CREATE TABLE node_info (
                           node_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '节点企业编号',
                           code VARCHAR(20) NOT NULL COMMENT '登录编码',
                           password VARCHAR(20) NOT NULL COMMENT '登录密码',
                           name VARCHAR(100) NOT NULL COMMENT '节点企业名称',
                           type INT NOT NULL COMMENT '企业类型：1: 养殖企业；2: 屠宰企业；3: 批发商；4: 零售商',
                           prov_id INT NOT NULL COMMENT '所在省编号',
                           city_id INT NOT NULL COMMENT '所在市编号',
                           address VARCHAR(200) NOT NULL COMMENT '节点企业地址',
                           business_id VARCHAR(30) NOT NULL COMMENT '营业执照代码（所有节点必填）',
                           ep_id VARCHAR(30) COMMENT '动物防疫条件合格证编号（养殖企业必填）',
                           eia_id VARCHAR(30) COMMENT '环境影响评价资质证书编号（屠宰、养殖企业必填）',
                           cir_id VARCHAR(30) COMMENT '食品流通许可证编号（批发商必填）',
                           fb_id VARCHAR(30) COMMENT '食品经营许可证编号（批发商、零售商必填）',
                           corporation VARCHAR(10) NOT NULL COMMENT '法定代表人',
                           telephone VARCHAR(20) NOT NULL COMMENT '联系电话',
                           reg_date DATE NOT NULL COMMENT '注册日期',
                           remarks VARCHAR(200) COMMENT '备注',
                           FOREIGN KEY (prov_id) REFERENCES province(prov_id),
                           FOREIGN KEY (city_id) REFERENCES city(city_id)
) COMMENT = '节点企业信息表';

-- 养殖企业产品批号信息表
CREATE TABLE farm_batch (
                            fb_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '编号（代替batch_id与farm_id的复合主键）',
                            batch_id VARCHAR(20) NOT NULL COMMENT '所属养殖企业产品批号',
                            farm_id INT NOT NULL COMMENT '所属养殖企业编号',
                            type VARCHAR(20) NOT NULL COMMENT '产品品种',
                            agc_id VARCHAR(30) NOT NULL COMMENT '动物检疫合格证编号',
                            test_name VARCHAR(20) NOT NULL COMMENT '官方检疫员名称',
                            batch_date DATE NOT NULL COMMENT '批号录入日期',
                            state INT NOT NULL DEFAULT 1 COMMENT '批号状态：1: 待发布；2: 已发布；3: 已下架',
                            remarks VARCHAR(200) COMMENT '备注',
                            FOREIGN KEY (farm_id) REFERENCES node_info(node_id)
) COMMENT = '养殖企业产品批号信息表';

-- 屠宰企业产品批号信息表
CREATE TABLE slau_batch (
                            sb_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '编号（代替batch_id与slau_id的复合主键）',
                            batch_id VARCHAR(20) NOT NULL COMMENT '所属屠宰企业产品批号',
                            slau_id INT NOT NULL COMMENT '所属屠宰企业编号',
                            fb_id INT NOT NULL COMMENT '所属养殖企业进场编号',
                            type VARCHAR(20) NOT NULL COMMENT '产品品种',
                            qua_id VARCHAR(30) NOT NULL COMMENT '肉类检验检疫合格证编号',
                            test_name VARCHAR(20) NOT NULL COMMENT '官方检验员名称',
                            batch_date DATE NOT NULL COMMENT '批号录入日期',
                            state INT NOT NULL DEFAULT 1 COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
                            remarks VARCHAR(200) COMMENT '备注',
                            FOREIGN KEY (slau_id) REFERENCES node_info(node_id),
                            FOREIGN KEY (fb_id) REFERENCES farm_batch(fb_id)
) COMMENT = '屠宰企业产品批号信息表';

-- 批发商产品批号信息表
CREATE TABLE whol_batch (
                            wb_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '编号（代替batch_id与whol_id的复合主键）',
                            batch_id VARCHAR(20) NOT NULL COMMENT '所属批发商产品批号',
                            whol_id INT NOT NULL COMMENT '所属批发商编号',
                            sb_id INT NOT NULL COMMENT '所属屠宰企业进场编号',
                            type VARCHAR(20) NOT NULL COMMENT '产品品种',
                            batch_date DATE NOT NULL COMMENT '批号录入日期',
                            state INT NOT NULL DEFAULT 1 COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
                            remarks VARCHAR(200) COMMENT '备注',
                            FOREIGN KEY (whol_id) REFERENCES node_info(node_id),
                            FOREIGN KEY (sb_id) REFERENCES slau_batch(sb_id)
) COMMENT = '批发商产品批号信息表';

-- 零售商产品批号信息表
CREATE TABLE reta_batch (
                            rb_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '编号（代替batch_id与reta_id的复合主键）',
                            batch_id VARCHAR(20) NOT NULL COMMENT '所属零售商产品批号',
                            reta_id INT NOT NULL COMMENT '所属零售商编号',
                            wb_id INT NOT NULL COMMENT '所属批发商进场编号',
                            type VARCHAR(20) NOT NULL COMMENT '产品品种',
                            batch_date DATE NOT NULL COMMENT '批号录入日期',
                            source_id VARCHAR(20) COMMENT '溯源标识码',
                            source_qr MEDIUMTEXT COMMENT '溯源二维码',
                            state INT NOT NULL DEFAULT 1 COMMENT '批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架',
                            remarks VARCHAR(200) COMMENT '备注',
                            FOREIGN KEY (reta_id) REFERENCES node_info(node_id),
                            FOREIGN KEY (wb_id) REFERENCES whol_batch(wb_id)
) COMMENT = '零售商产品批号信息表';

-- 省行政区信息表
CREATE TABLE province (
                          prov_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '省行政区编号',
                          prov_name VARCHAR(10) NOT NULL COMMENT '省行政区名称',
                          remarks VARCHAR(200) COMMENT '备注'
) COMMENT = '省行政区信息表';

-- 市行政区域信息表
CREATE TABLE city (
                      city_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '市行政区编号',
                      city_name VARCHAR(10) NOT NULL COMMENT '市行政区名称',
                      prov_id INT NOT NULL COMMENT '所属省编号',
                      remarks VARCHAR(200) COMMENT '备注',
                      FOREIGN KEY (prov_id) REFERENCES province(prov_id)
) COMMENT = '市行政区域信息表';

-- 系统管理员表
CREATE TABLE admin (
                       admin_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员编号',
                       admin_name VARCHAR(20) NOT NULL COMMENT '管理员登录名称',
                       admin_password VARCHAR(20) NOT NULL COMMENT '管理员登录密码',
                       remarks VARCHAR(200) COMMENT '备注'
) COMMENT = '系统管理员表';




-- 插入省行政区数据
INSERT INTO province (prov_name, remarks) VALUES
                                              ('北京市', '直辖市'),
                                              ('上海市', '直辖市'),
                                              ('广东省', '华南地区'),
                                              ('江苏省', '华东地区'),
                                              ('四川省', '西南地区');

-- 插入市行政区域数据
INSERT INTO city (city_name, prov_id, remarks) VALUES
                                                   ('北京市', 1, '首都'),
                                                   ('上海市', 2, '经济中心'),
                                                   ('广州市', 3, '广东省会'),
                                                   ('深圳市', 3, '经济特区'),
                                                   ('南京市', 4, '江苏省会'),
                                                   ('成都市', 5, '四川省会');

-- 插入节点企业信息数据
INSERT INTO node_info (code, password, name, type, prov_id, city_id, address, business_id, ep_id, eia_id, cir_id, fb_id, corporation, telephone, reg_date, remarks) VALUES
                                                                                                                                                                        ('farm001', '123456', '阳光养殖有限公司', 1, 3, 3, '广州市天河区养殖路1号', '91440101MA5ABC1234', 'GD12345678', 'EIA2023001', NULL, NULL, '张三', '13800138001', '2020-01-15', '专业肉鸡养殖'),
                                                                                                                                                                        ('slau001', '123456', '鲜美屠宰加工厂', 2, 3, 4, '深圳市宝安区屠宰路2号', '91440300MA5DEF5678', NULL, 'EIA2023002', NULL, NULL, '李四', '13800138002', '2020-03-20', '现代化屠宰加工'),
                                                                                                                                                                        ('whol001', '123456', '华南批发市场', 3, 3, 3, '广州市白云区批发路3号', '91440101MA5GHI9012', NULL, NULL, 'SP2023001', 'JY2023001', '王五', '13800138003', '2020-05-10', '大型肉类批发'),
                                                                                                                                                                        ('reta001', '123456', '惠民超市', 4, 3, 4, '深圳市南山区零售路4号', '91440300MA5JKL3456', NULL, NULL, NULL, 'JY2023002', '赵六', '13800138004', '2020-07-25', '社区连锁超市'),
                                                                                                                                                                        ('farm002', '123456', '绿色生态养殖场', 1, 4, 5, '南京市江宁区生态路5号', '91320101MA5MNO7890', 'JS87654321', 'EIA2023003', NULL, NULL, '钱七', '13800138005', '2020-09-30', '有机养殖基地');

-- 插入养殖企业产品批号数据
INSERT INTO farm_batch (batch_id, farm_id, type, agc_id, test_name, batch_date, state, remarks) VALUES
                                                                                                    ('F202301001', 1, '三黄鸡', 'AG202301001', '检疫员A', '2023-01-15', 2, '首批出栏'),
                                                                                                    ('F202302001', 1, '清远鸡', 'AG202302001', '检疫员B', '2023-02-20', 2, '优质品种'),
                                                                                                    ('F202303001', 5, '草鸡', 'AG202303001', '检疫员C', '2023-03-10', 1, '待出栏'),
                                                                                                    ('F202304001', 1, '乌鸡', 'AG202304001', '检疫员A', '2023-04-05', 2, '药用价值高'),
                                                                                                    ('F202305001', 5, '蛋鸡', 'AG202305001', '检疫员D', '2023-05-12', 3, '已下架处理');

-- 插入屠宰企业产品批号数据
INSERT INTO slau_batch (batch_id, slau_id, fb_id, type, qua_id, test_name, batch_date, state, remarks) VALUES
                                                                                                           ('S202301001', 2, 1, '白条鸡', 'QU202301001', '检验员A', '2023-01-16', 3, '已完成加工'),
                                                                                                           ('S202302001', 2, 2, '分割鸡', 'QU202302001', '检验员B', '2023-02-21', 3, '优质分割'),
                                                                                                           ('S202303001', 2, 4, '乌鸡', 'QU202303001', '检验员C', '2023-04-06', 2, '待确认'),
                                                                                                           ('S202304001', 2, 3, '草鸡', 'QU202304001', '检验员A', '2023-03-11', 1, '新建批次'),
                                                                                                           ('S202305001', 2, 1, '鸡翅', 'QU202305001', '检验员D', '2023-01-17', 4, '已下架');

-- 插入批发商产品批号数据
INSERT INTO whol_batch (batch_id, whol_id, sb_id, type, batch_date, state, remarks) VALUES
                                                                                        ('W202301001', 3, 1, '白条鸡', '2023-01-17', 3, '已确认收货'),
                                                                                        ('W202302001', 3, 2, '鸡腿', '2023-02-22', 3, '热销产品'),
                                                                                        ('W202303001', 3, 3, '乌鸡', '2023-04-07', 2, '待确认'),
                                                                                        ('W202304001', 3, 4, '草鸡', '2023-03-12', 1, '新建订单'),
                                                                                        ('W202305001', 3, 5, '鸡翅', '2023-01-18', 4, '已下架处理');

-- 插入零售商产品批号数据
INSERT INTO reta_batch (batch_id, reta_id, wb_id, type, batch_date, source_id, source_qr, state, remarks) VALUES
                                                                                                              ('R202301001', 4, 1, '新鲜鸡肉', '2023-01-18', 'SRC202301001', 'QR_CODE_001', 3, '已上架销售'),
                                                                                                              ('R202302001', 4, 2, '优质鸡腿', '2023-02-23', 'SRC202302001', 'QR_CODE_002', 3, '热销中'),
                                                                                                              ('R202303001', 4, 3, '养生乌鸡', '2023-04-08', 'SRC202303001', 'QR_CODE_003', 2, '待确认收货'),
                                                                                                              ('R202304001', 4, 4, '生态草鸡', '2023-03-13', 'SRC202304001', 'QR_CODE_004', 1, '新建入库'),
                                                                                                              ('R202305001', 4, 5, '冷冻鸡翅', '2023-01-19', 'SRC202305001', 'QR_CODE_005', 4, '已下架');

-- 插入系统管理员数据
INSERT INTO admin (admin_name, admin_password, remarks) VALUES
                                                            ('admin', 'admin123', '超级管理员'),
                                                            ('manager1', 'mgr123456', '业务管理员'),
                                                            ('auditor1', 'audit123', '审计管理员'),
                                                            ('operator1', 'oper123', '操作员');

-- 修改系统管理员表密码字段长度（推荐）
ALTER TABLE admin
    MODIFY COLUMN admin_password VARCHAR(255) NOT NULL COMMENT '管理员登录密码（BCrypt加密）';

-- 修改节点企业信息表密码字段长度（推荐）
ALTER TABLE node_info
    MODIFY COLUMN password VARCHAR(255) NOT NULL COMMENT '登录密码（BCrypt加密）';



-- 将 DATE 改为 DATETIME 并使用 CURRENT_TIMESTAMP
ALTER TABLE whol_batch
    MODIFY COLUMN batch_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批号录入日期';

-- 将 DATE 改为 DATETIME 并使用 CURRENT_TIMESTAMP
ALTER TABLE farm_batch
    MODIFY COLUMN batch_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批号录入日期';

-- 将 DATE 改为 DATETIME 并使用 CURRENT_TIMESTAMP
ALTER TABLE slau_batch
    MODIFY COLUMN batch_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批号录入日期';

-- 将 DATE 改为 DATETIME 并使用 CURRENT_TIMESTAMP
ALTER TABLE reta_batch
    MODIFY COLUMN batch_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批号录入日期';

-- 将 DATE 改为 DATETIME 并使用 CURRENT_TIMESTAMP
ALTER TABLE node_info
    MODIFY COLUMN reg_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批号录入日期';




-- 为 node_info 表添加逻辑删除字段
ALTER TABLE node_info
    ADD COLUMN status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
    ADD COLUMN delete_time DATETIME NULL COMMENT '删除时间',
    ADD COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

-- 为现有数据设置默认状态
UPDATE node_info SET status = 0 WHERE status IS NULL;



-- 节点企业信息表索引优化
ALTER TABLE node_info
    ADD INDEX idx_type (type),
    ADD INDEX idx_prov_city (prov_id, city_id),
    ADD INDEX idx_status (status),
    ADD INDEX idx_name (name);

-- 养殖企业产品批号信息表索引优化
ALTER TABLE farm_batch
    ADD INDEX idx_farm_id (farm_id),
    ADD INDEX idx_state (state),
    ADD INDEX idx_farm_state (farm_id, state),
    ADD INDEX idx_batch_id (batch_id);

-- 屠宰企业产品批号信息表索引优化
ALTER TABLE slau_batch
    ADD INDEX idx_slau_id (slau_id),
    ADD INDEX idx_fb_id (fb_id),
    ADD INDEX idx_state (state),
    ADD INDEX idx_slau_state (slau_id, state),
    ADD INDEX idx_batch_id (batch_id);

-- 批发商产品批号信息表索引优化
ALTER TABLE whol_batch
    ADD INDEX idx_whol_id (whol_id),
    ADD INDEX idx_sb_id (sb_id),
    ADD INDEX idx_state (state),
    ADD INDEX idx_whol_state (whol_id, state),
    ADD INDEX idx_batch_id (batch_id);

-- 零售商产品批号信息表索引优化
ALTER TABLE reta_batch
    ADD INDEX idx_reta_id (reta_id),
    ADD INDEX idx_wb_id (wb_id),
    ADD INDEX idx_state (state),
    ADD INDEX idx_source_id (source_id),
    ADD INDEX idx_reta_state (reta_id, state),
    ADD INDEX idx_batch_id (batch_id);

-- 行政区信息表索引优化
ALTER TABLE province
    ADD INDEX idx_prov_name (prov_name);

ALTER TABLE city
    ADD INDEX idx_prov_id (prov_id),
    ADD INDEX idx_city_name (city_name);

-- 管理员表索引优化
ALTER TABLE admin
    ADD INDEX idx_admin_name (admin_name);

ALTER TABLE node_info MODIFY reg_date DATETIME NULL;
ALTER TABLE reta_batch MODIFY batch_date DATETIME NULL;
ALTER TABLE whol_batch MODIFY batch_date DATETIME NULL;
ALTER TABLE slau_batch MODIFY batch_date DATETIME NULL;
ALTER TABLE farm_batch MODIFY batch_date DATETIME NULL;


-- 为管理员表添加角色字段
ALTER TABLE admin
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'OPERATOR' COMMENT '管理员角色：SUPER_ADMIN-超级管理员, ADMIN-高级管理员, OPERATOR-普通操作员';

ALTER TABLE admin
    MODIFY role VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '管理员角色：ADMIN-高级管理员, NORMAL-普通管理员';

-- 更新现有管理员的角色
UPDATE admin SET role = 'ADMIN' WHERE admin_name = 'admin';

ALTER TABLE slau_batch MODIFY COLUMN batch_date datetime NULL;
ALTER TABLE whol_batch MODIFY COLUMN batch_date datetime NULL;
ALTER TABLE reta_batch MODIFY COLUMN batch_date datetime NULL;