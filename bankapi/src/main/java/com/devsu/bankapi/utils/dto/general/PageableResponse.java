package com.devsu.bankapi.utils.dto.general;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse extends AbstractResponse {

    private List<AbstractResponse> dataList;

    private Integer currentPage;

    private Long totalItems;

    private Integer totalPages;

    public PageableResponse(ErrorList errors){
        super(errors);
    }

}
