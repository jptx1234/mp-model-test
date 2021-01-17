package me.jptx.mp.model.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 学生实体（继承MP中的Model，带有log对象）
 */
@Data
@Accessors(chain = true)
@TableName("h2student")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class H2StudentWithLog extends Model<H2StudentWithLog> {


  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1L;

}
