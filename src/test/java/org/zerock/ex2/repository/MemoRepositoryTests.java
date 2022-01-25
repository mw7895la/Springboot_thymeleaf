package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;


    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i ->{
            Memo memo = Memo.builder().memoText("sample Memo" +i).build();
            memoRepository.save(memo);
        });
    }


    @Test
    public void testSelect(){

        //데이터베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("===============================");

        if(result.isPresent()){
            Memo memo = result.get();

            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2(){
        //데이터베이스에 존재하는 mno
        Long mno=100L;

        Memo memo = memoRepository.getOne(mno);     //실제 객체를 사용하는 순간에 SQL이 동작함.
        System.out.println("===============================");
        System.out.println(memo);
    }

    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("100th is updated #2.").build();



        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
        //Spring Data JPA이용시 페이지 처리는 반드시 0 부터 시작.
        //1페이지에 데이터 10개를 가져오기위해  0,10
        //PageRequest는 protected로 되어있어서 new로 객체를 생성할 수 없음. of()로 생성.
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("---------------------------------------------");

        System.out.println("Total Page : "+result.getTotalPages()); //총 몇 페이지??

        System.out.println("Total Count : "+result.getTotalElements()); //전체 개수

        System.out.println("Page Number : "+result.getNumber());        //현재 페이지 번호 0부터 시작

        System.out.println("Page Size : "+result.getSize());            //페이지당 데이터 개수

        System.out.println("has next Page ?  :"+result.hasNext());      //다음 페이지 존재 여부

        System.out.println("first Page ? : "+result.isFirst());     //시작 페이지(0) 여부

        System.out.println("==================================================");

        //getContent() 실제 페이지의 데이터를 처리하는 메서드
        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }

    }

    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);        //and를 이용한 연결.

        Pageable pageable = PageRequest.of(3,10,sortAll);

        Page<Memo> result=memoRepository.findAll(pageable);


        //getContent() , get()   실제 컨텐츠를 가지고 오는 메서드  getContent()는 List<Entity>반환 // get()은 Stream<Entity>
        result.get().forEach(memo ->{
            System.out.println(memo);
        });
    }


    @Test
    public void testQueryMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(75L,85L);

        for(Memo memo: list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPagable(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);
        result.get().forEach(memo -> System.out.println(memo));


    }


    //어느 숫자보다 작은 데이터들을 삭제, @Commit은 삭제 후 남은 데이터들로 최종 커밋, 안하게되면 다시 롤백되어 결과가 반영되지 않음.
    //@Transactional은 아래 delete의 경우 select문으로 데이터들을 가져오는 작업과 각 엔티티를 삭제하는 작업때문에 붙여준 어노테이션.
    @Test
    @Transactional
    @Commit
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testSelect_2(){
        Pageable pageable = PageRequest.of(0,15);
        List<Memo> result = memoRepository.getListAsec(10L,50L,pageable);
        for(Memo memo: result){
            System.out.println(memo);
        }
    }

    @Test
    public void testUpdate_2(){
        Memo memo= Memo.builder().mno(99L).memoText("0125").build();
        int result = memoRepository.updateMemoText(memo.getMno(),memo.getMemoText());
        System.out.println(result);
    }
}
