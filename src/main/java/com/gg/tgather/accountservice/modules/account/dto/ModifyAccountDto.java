package com.gg.tgather.accountservice.modules.account.dto;

import com.gg.tgather.commonservice.enums.EnumMapperValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 계정 정보를 수정하기 위한 Dto
 * 여행 테마 정보도 함께 내려 보내준다. ( 화면에서 보여줄 용도, 더 추가할 가능성이 있기 때문. )
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAccountDto {

    private CustomAccountDto accountDto;

    private List<EnumMapperValue> travelThemes;

}
