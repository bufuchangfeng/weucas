package com.ucas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuchen
 * @since 2019-10-31
 */
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LibraryUser extends Model<LibraryUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String openid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
