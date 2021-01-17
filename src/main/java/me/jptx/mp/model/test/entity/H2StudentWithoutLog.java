package me.jptx.mp.model.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.ModelWithoutLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 学生实体（不带log对象）
 */
@Data
@Accessors(chain = true)
@TableName("h2student")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class H2StudentWithoutLog extends ModelWithoutLog<H2StudentWithoutLog> {


  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 2L;


}
