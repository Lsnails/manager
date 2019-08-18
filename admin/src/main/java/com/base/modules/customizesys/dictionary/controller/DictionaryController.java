package com.base.modules.customizesys.dictionary.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.base.common.utils.PageUtils;
import com.base.common.utils.R;
import com.base.common.validator.ValidatorUtils;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;
import com.base.modules.customizesys.dictionary.entity.DictonarySearchVo;
import com.base.modules.customizesys.dictionary.service.DictionaryService;
import com.base.modules.customizesys.helper.Constant;
import com.base.modules.sys.controller.AbstractController;
import com.base.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;



/**
 * 业务数据字典包含多层次数据
 *
 * @author huanw
 * @email 
 * @date 2019-02-11 17:09:10
 */
@RestController
@RequestMapping("/cms/dictionary")
@Api(tags="后台业务数据字典管理")
public class DictionaryController  extends AbstractController {
    @Autowired
    private DictionaryService dictionaryService;
    
    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("业务数据字典列表")
    @RequiresPermissions("system:dictionary:list")
    public List<DictionaryEntity> list(@RequestBody DictionaryEntity dictionary){
    	Map<String, Object> params=null;
    	List<DictionaryEntity> dictionaryList = dictionaryService.queryDictionaryEntityList(params,dictionary);
        return dictionaryList;
    }
    /**
     * 列表带分页暂时未用，勿删
     */
    @GetMapping("/list1")
    @ApiOperation("业务数据字典列表-暂时未用")
    @RequiresPermissions("system:dictionary:list")
    public R list1(@RequestParam Map<String, Object> params){
    	//分页查询一级分类，然后根据一级分类递归查询子分类
		Page<DictionaryEntity> page = dictionaryService.queryPage(params);
		List<DictionaryEntity> dictionaryList = (List<DictionaryEntity>)page.getRecords();
        List<DictionaryEntity> queryDictionaryEntityList = dictionaryService.queryDictionaryEntityList(dictionaryList);
        //分页存在问题，启用该方法需要测试
        return PageUtils.convertFrom(queryDictionaryEntityList,queryDictionaryEntityList.size());
    }

    /**
     * 信息
     */
    @GetMapping("/info/{dictId}")
    @ApiOperation("后台数据字典信息")
    @RequiresPermissions("system:dictionary:info")
    public R info(@PathVariable("dictId") String dictId){
        DictionaryEntity dictionary = dictionaryService.selectById(dictId);
        String parentId = dictionary.getParentId();
       //设置父节点名称
        if(StringUtils.isNotBlank(parentId)) {
        	String parentName = dictionaryService.getDictionaryNameByDictId(parentId);
            dictionary.setParentName(parentName);
        }else {
        	dictionary.setParentName("一級分類");
        }
        
        return R.ok().put("dictionary", dictionary);
    }
    
    /**
	 * 后台数据字典完整分类树(添加、修改分类)
	 */
	@GetMapping("/allselect")
	@ApiOperation("后台数据字典完整分类树")
	public R allSelect(String language){
		//查询列表数据
		EntityWrapper<DictionaryEntity> entityWrapper = new EntityWrapper<DictionaryEntity>();
		entityWrapper.eq("enabled", "1");
		entityWrapper.eq(StringUtils.isNotBlank(language), "language", language);
		List<DictionaryEntity> dictionaryList = dictionaryService.selectList(entityWrapper);
		//添加顶级菜单
		DictionaryEntity root = new DictionaryEntity();
		root.setDictId(null);
		root.setName("一級分類");
		root.setParentId(null);
		root.setOpen(true);
		dictionaryList.add(root);
		return R.ok().put("dictionaryList", dictionaryList);
	}
    /**
	 * 课程、教授等分类树应用
	 */
	@GetMapping("/select")
	@ApiOperation("课程、教授等分类树应用")
	public R select(String language,String moduleId){
		List<DictionaryEntity> dictionaryList =new ArrayList<>();
		DictionaryEntity dictionaryEntity =new DictionaryEntity();
		EntityWrapper<DictionaryEntity> entityWrapper = new EntityWrapper<DictionaryEntity>();
		entityWrapper.eq("enabled", "1");
		entityWrapper.eq(StringUtils.isNotBlank(language), "language", language);
		if (Constant.MODULE_COURSE_1.equals(moduleId)) {
			// 课程 英文的分类父节点也必须是"学院课程"
			// TODO Auto-generated method stub  字典树分类，课程一级菜单必须叫“学院课程”
			entityWrapper.eq("name", Constant.COURSE_CLASSIFICATION);
			dictionaryEntity = dictionaryService.selectOne(entityWrapper);
		} else if (Constant.MODULE_PROFESSOR_2.equals(moduleId)) {
			// 教授
			// 字典树分类，课程一级菜单必须叫“学院师资”
			entityWrapper.eq("name", Constant.PROFESSOR_CLASSIFICATION);
			dictionaryEntity = dictionaryService.selectOne(entityWrapper);
		}else if(Constant.MODULE_REALM_5.equals(moduleId)) {
			// 知识库领域
			// TODO Auto-generated method stub 
			dictionaryEntity=null;
		}
		if(dictionaryEntity!=null) {
			dictionaryList.add(dictionaryEntity);
		}
		List<DictionaryEntity> queryDictionaryEntityList = dictionaryService.queryDictionaryEntityList(dictionaryList);
		return R.ok().put("dictionaryList", queryDictionaryEntityList);
	}

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("新增数据字典")
//    @CustomizeCacheAnnotation(value="新增字典触发缓存",moduleId = "9", type = "add")
    @RequiresPermissions("system:dictionary:save")
    public R save(@RequestBody DictionaryEntity dictionary){
    	String dictId = UUIDUtils.getRandomUUID();
    	dictionary.setDictId(dictId);
        dictionaryService.insert(dictionary);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改数据字典")
//    @CustomizeCacheAnnotation(value="修改字典程内容触发缓存",moduleId = "9", type = "updateVo")
    @RequiresPermissions("system:dictionary:update")
    public R update(@RequestBody DictionaryEntity dictionary){
        ValidatorUtils.validateEntity(dictionary);
        dictionaryService.updateAllColumnById(dictionary);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除数据字典")
//    @CustomizeCacheAnnotation(value="删除字典触发缓存",moduleId = "9", type = "delete")
    @RequiresPermissions("system:dictionary:delete")
    public R delete(@RequestBody DictonarySearchVo dictonarySearch){
    	String[] dictIds = dictonarySearch.getDictIds();
    	String[] moduleIds = dictonarySearch.getModuleIds();//删除字典时处理其他联动业务
    	List<String> dictIdList = Arrays.asList(dictIds);
        dictionaryService.deleteBatchIds(dictIdList);
        //moduleIds 明确字典于0：新闻 1：课程 2：教授 3：案例 4:历届学友 5：知识库的领域 解耦
//        if(moduleIds!=null) {
//        	for (int i = 0; i < moduleIds.length; i++) {
//    			String moduleId=moduleIds[i];
//    			if(Constant.MODULE_COURSE_1.equals(moduleId)) {
//    				//课程
//    				CourseEntity course=new CourseEntity();
//    				course.setLastmodifyby(this.getUsername());
//    				course.setLastmodifytimeString(ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
//    				courseService.updateCourseByClassification(dictIdList, course);
//    			}else if(Constant.MODULE_PROFESSOR_2.equals(moduleId)) {
//    				//教授与分类解耦，将教授的分类设置为空，并且修改状态等信息
//    				ProfessorEntity professor =new ProfessorEntity();
//    				professor.setLastmodifyby(this.getUsername());
//    				professor.setLastmodifytimeString(ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
//    				professorService.updateProfessorByClassification(dictIdList, professor);
//    			}else if(Constant.MODULE_REALM_5.equals(moduleId)) {
//    				//待完成知识库：领域
//    				// TODO Auto-generated method stub
//    			}
//    		}
//        }
        return R.ok();
    }
    
    @GetMapping("/checkIsLeafnode/{dictId}")
    @ApiOperation("检查是否是叶子节点")
    public R checkIsLeafnode(@PathVariable String dictId){
    	List<String> dictIdList=new ArrayList<>();
    	dictIdList.add(dictId);
        //查询分类是否是叶子节点,true :叶子节点
    	boolean checkIsLeafnode = dictionaryService.checkIsLeafnode(dictIdList);
    	if(checkIsLeafnode) {
    		return R.ok();
    	}else {
    		Map<String,Object> map=new HashMap<>();
    		map.put("code", "601");
    		map.put("msg", "非叶子节点");
    		return R.ok(map);
    	}
    }
    
    @PostMapping("/checkDictionaryIsOccupy")
    @ApiOperation("检查是否被占用")
    public R queryDictionaryIsOccupy(@RequestBody String[] dictIds){
        //查询分类是否被其他模块引用
    	List<DictionaryEntity> dictionaryList = dictionaryService.queryDictionaryIsOccupy(Arrays.asList(dictIds));
    	R r=null;
    	if(dictionaryList==null || dictionaryList.isEmpty()) {
    		r=new R();
    	}else {
    		//封装引用数据字典的模块数据，显示前端
    		Map<String, List<String>> map = dictionaryService.getDictionaryOccupyData(dictionaryList);
        	Map<String,Object> mapMsg=new HashMap<>();
        	mapMsg.put("code", "601");
        	mapMsg.put("msg", "此分类被占用");
        	r = R.ok(mapMsg).put("data", map);
    	}
    	return r;
    }
}
