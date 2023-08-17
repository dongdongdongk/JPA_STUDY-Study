package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println("테스트 테스트" + memoRepository.getClass().getName());

    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample......" + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        //데이터 베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("============================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10);   // 현재 페이지 0 ,  페이지 데이터 10
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("=====================================");
        System.out.println("TotalPage : " + result.getTotalPages()); // 총 페이지
        System.out.println("TotalCount : " + result.getTotalElements()); // 전체개수
        System.out.println("Page Number : " + result.getNumber()); // 현재 페이지 번호
        System.out.println("Page Size : " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("has next Page? : " + result.hasNext()); // 다음 페이지 존재여부
        System.out.println("first page? : " + result.isFirst()); // 시작 페이지(0) 인가
        //실제 페이지 데이터 불러오기
        System.out.println("=====================================");
        for(Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testSort2() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); // and 를 이용한 연결
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        List<Memo> memoList = result.getContent();
        for (Memo memo : memoList) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort3() {
        Sort sort1 = Sort.by("mno").descending();    // mno 필드의 값을 역순으로 정렬하도록
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        List<Memo> memoList = result.getContent();

        for (Memo memo : memoList) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethod() {
        memoRepository.deleteByMnoLessThan(10L);
    }
}
