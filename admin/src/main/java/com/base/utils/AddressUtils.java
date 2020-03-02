package com.base.utils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressUtils {
	/** * 解析地址 * @author lin * @param address * @return */
	public static List<Map<String, String>> addressResolution(String address) {
//		String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
		String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?行政单位|.+盟|市辖区|.*?市|.*?县)";
		Matcher m = Pattern.compile(regex).matcher(address);
		String province = null, city = null, county = null, town = null, village = null;
		List<Map<String, String>> table = new ArrayList<Map<String, String>>();
		Map<String, String> row = null;
		int i=0;
		while (m.find()) {
			if(i>0) {
				break;
			}
			row = new LinkedHashMap<String, String>();
			province = m.group("province");
			row.put("province", province == null ? "" : province.trim());
			city = m.group("city");
			row.put("city", city == null ? "" : city.trim());
			/*county = m.group("county");
			row.put("county", county == null ? "" : county.trim());
			town = m.group("town");
			row.put("town", town == null ? "" : town.trim());
			village = m.group("village");
			row.put("village", village == null ? "" : village.trim());*/
			table.add(row);
			i++;
		}
		return table;
	}

	public static void main(String[] args) {
		System.out.println(addressResolution("湖北省武汉市洪山区"));
		System.out.println(addressResolution("湖北省恩施土家族苗族自治州恩施市"));
		System.out.println(addressResolution("北京市市辖区朝阳区"));
		System.out.println(addressResolution("内蒙古自治区兴安盟科尔沁右翼前旗"));
		System.out.println(addressResolution("西藏自治区日喀则地区日喀则市"));
		System.out.println(addressResolution("海南省直辖县级行政单位中沙群岛的岛礁及其海域"));
		System.out.println(addressResolution("甘肃省天水市秦州区城区天水市秦州区坚家河名城瑞苑（正大水岸都市西侧）3号楼1单元17楼2号"));
		System.out.println(addressResolution("四川省重庆市九龙坡区华岩镇华玉路444号田野玉竹花园7栋22-2"));
		System.out.println(addressResolution("四川省德阳市旌阳区城区秀山街75号3栋1单元404室（御锦苑小区）"));
		System.out.println(addressResolution("广东省广州市海珠区马涌直街61号之四301"));
		System.out.println(addressResolution("湖南省株洲市攸县酒埠江镇酒埠江镇酒江村酒江村"));
		System.out.println(addressResolution("青海省西宁市城东区城区三榆龙湖花园12号楼"));
		System.out.println(addressResolution("湖北省宜昌市远安县鸣凤镇东门路二十七号"));
		System.out.println(addressResolution("江苏省常州市钟楼区城区劳动西路293号（先锋汽车装璜）"));
		System.out.println(addressResolution("四川省绵阳市梓潼县双板乡小学校"));
		System.out.println(addressResolution("海南省三亚市吉阳区吉阳镇田独村颂和水库南岸隆平高科小区C栋102号"));
		System.out.println(addressResolution("辽宁省沈阳市沈北新区清水台镇清水回迁楼B区146-28号楼三单元三楼一号"));
		System.out.println(addressResolution("河北省唐山市古冶区习家套乡东李家套村和睦街125号"));
		System.out.println(addressResolution("湖南省长沙市岳麓区城区银晟花园4栋206"));
		System.out.println(addressResolution("河南省周口市沈丘县石槽集乡陈庄行政村郭楼村二队"));
		System.out.println(addressResolution("辽宁省抚顺市望花区城区古城子千台山上56栋23号"));
		System.out.println(addressResolution("四川省遂宁市蓬溪县县城内大转盘陈家湾公租房4栋2楼1号"));
		System.out.println(addressResolution("福建省三明市永安市槐南乡槐南村车路下65号"));
		System.out.println(addressResolution("江苏省泰州市姜堰区白米镇江苏泰州市姜堰市白米镇白米镇白米村4组14号"));
		System.out.println(addressResolution("福建省漳州市龙海市角美镇福建省漳州市龙海市角美镇保生路华坤花园4B梯1112"));
		System.out.println(addressResolution("山西省太原市万柏林区万柏林街道太原市万柏林区和平北路晋机新友谊二小区36楼3单元401"));
		
		
		System.out.println(addressResolution("江苏省泰州市姜堰区白米镇江苏泰州市姜堰市白米镇白米镇白米村4组14号"));
		List<Map<String, String>> addressResolution = addressResolution("江苏省泰州市姜堰区白米镇江苏泰州市姜堰市白米镇白米镇白米村4组14号");
		addressResolution.stream().forEach(i->{
			System.out.println(i.get("province"));
			System.out.println(i.get("city"));
		});


	}
}
