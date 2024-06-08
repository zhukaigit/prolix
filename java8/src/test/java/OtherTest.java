import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class OtherTest {

  @Test
  public void testClassLoader() throws ClassNotFoundException {
    String json = "{\"bizSelfRangeMul\":0,\"feeSourceType\":\"0\",\"importCarFlag\":true,\"endDate\":0,\"firstYearInsureFlag\":true,\"bcHasSameCity\":true,\"userPhoneNo\":\"15601784144\",\"paStandardFeeClaimRatio\":0.654379,\"coverageList\":\"[{\\\"amount\\\":\\\"296022.52\\\",\\\"baseRiderType\\\":\\\"BASE\\\",\\\"flag\\\":\\\"insure_plan_for_out_of_date\\\",\\\"greenWayCoverage\\\":false,\\\"isNonDeductible\\\":0,\\\"policyProductCode\\\":\\\"981_0001\\\",\\\"premium\\\":\\\"8752.33\\\",\\\"premiumRate\\\":\\\"0\\\",\\\"productDefCode\\\":\\\"981\\\",\\\"productDefName\\\":\\\"新能源汽车损失保险\\\",\\\"productId\\\":1510052324,\\\"pureRiskPremium\\\":\\\"5510.732726\\\",\\\"seats\\\":\\\"0\\\",\\\"showOrder\\\":\\\"10\\\",\\\"standPremium\\\":\\\"6483.21\\\"},{\\\"amount\\\":\\\"1000000\\\",\\\"baseRiderType\\\":\\\"BASE\\\",\\\"flag\\\":\\\"insure_plan_for_out_of_date\\\",\\\"greenWayCoverage\\\":false,\\\"isNonDeductible\\\":0,\\\"policyProductCode\\\":\\\"982_0001\\\",\\\"premium\\\":\\\"1061.1\\\",\\\"premiumRate\\\":\\\"0\\\",\\\"productDefCode\\\":\\\"982\\\",\\\"productDefName\\\":\\\"新能源汽车第三者责任保险\\\",\\\"productId\\\":1510052325,\\\"pureRiskPremium\\\":\\\"668.1\\\",\\\"seats\\\":\\\"0\\\",\\\"showOrder\\\":\\\"11\\\",\\\"standPremium\\\":\\\"786\\\"},{\\\"amount\\\":\\\"10000\\\",\\\"baseRiderType\\\":\\\"BASE\\\",\\\"flag\\\":\\\"insure_plan_for_out_of_date\\\",\\\"greenWayCoverage\\\":false,\\\"isNonDeductible\\\":0,\\\"policyProductCode\\\":\\\"983_0001\\\",\\\"premium\\\":\\\"33.7\\\",\\\"premiumRate\\\":\\\"0\\\",\\\"productDefCode\\\":\\\"983\\\",\\\"productDefName\\\":\\\"新能源汽车车上人员责任保险\\\",\\\"productId\\\":1510052326,\\\"pureRiskPremium\\\":\\\"21.22\\\",\\\"seats\\\":\\\"0\\\",\\\"showOrder\\\":\\\"12\\\",\\\"standPremium\\\":\\\"24.96\\\"},{\\\"amount\\\":\\\"10000\\\",\\\"baseRiderType\\\":\\\"BASE\\\",\\\"flag\\\":\\\"insure_plan_for_out_of_date\\\",\\\"greenWayCoverage\\\":false,\\\"isNonDeductible\\\":0,\\\"policyProductCode\\\":\\\"984_0001\\\",\\\"premium\\\":\\\"85.51\\\",\\\"premiumRate\\\":\\\"0\\\",\\\"productDefCode\\\":\\\"984\\\",\\\"productDefName\\\":\\\"新能源汽车车上人员责任保险责任(乘客)\\\",\\\"productId\\\":1510052327,\\\"pureRiskPremium\\\":\\\"53.84\\\",\\\"seats\\\":\\\"4\\\",\\\"showOrder\\\":\\\"13\\\",\\\"standPremium\\\":\\\"63.34\\\"}]\",\"modelYearAndRegDateDuration\":-2,\"carOwnerGender\":\"M\",\"insurantEmailUsedTimes\":-1,\"plateColor\":\"1\",\"bizEffectiveDate\":20220102,\"noClaimAdjustRatioBiz\":1,\"policyHolderCertificateType\":\"I\",\"seatNum\":5,\"individualAudit\":false,\"insureTransferIntervalDay\":0,\"compelDiscount\":-1,\"insuredName\":\"徐道兵\",\"bizDiscount\":-1,\"deductScore\":-1,\"engineNo\":\"34547843578987\",\"selfUnderwriteRatio\":-1,\"utmSource\":\"direct_ref\",\"yearAndRegDateDuration\":-1,\"isTransfer\":false,\"campaignId\":\"1000095012\",\"selfChannelRatio\":-1,\"violationRegulationTimes\":-1,\"quoteId\":100000257380041,\"trafficAdjustRatio\":1,\"rbCode\":\"DZAAWI0018\",\"anteDateFlag\":false,\"bizInsurePeriod\":\"365\",\"ncdReasonCode\":\"11\",\"inputTotalFeeRate\":-1,\"doubleRatioDownLimit\":-1,\"policyHolderName\":\"徐道兵\",\"certificateType\":\"I\",\"noClaimYear\":-1,\"vehicleName\":\"蔚揽MAGOTAN VP GTE插电式混合动力旅行轿车\",\"gender\":\"M\",\"paSelfAdjust\":\"0\",\"actualPrice\":296022.52,\"bizSelfRatioIn\":\"1.35\",\"insurantCertificateType\":\"I\",\"paCustomerRate\":50,\"noClaimAdjustRatio\":1,\"trafficRatioFloatReason\":\"1\",\"premium\":4966.32,\"checkLevel\":\"quote_stage\",\"licenceType\":\"\",\"tYCodeFlag\":false,\"energyFlag\":true,\"vehicleModel\":\"DZAAWI0018\",\"paTargetClaimRatio\":0.589,\"channelFlag\":\"1036370023\",\"bcHasSameProvince\":true,\"bizSelfRangeMax\":1.35,\"isOffInsured\":true,\"vehicleLossAmountRange\":-1,\"insureFlag\":1,\"lastAcceptCompany\":\"UNKNOWN\",\"carOwnerPhone\":\"156******44\",\"carVesselTax\":\"1\",\"lossRatio\":-1,\"paSelfAdjustIn\":\"0\",\"plateNoConsistentWithPlatform\":true,\"insuredLastDidiDriverIntervalMonths\":-1,\"loanedFlag\":false,\"userIp\":\"58.33.142.50\",\"claimAmountChoice\":0,\"instantFlag\":false,\"claimAdjustLevel\":0,\"insurantIdNo\":\"3****************9\",\"vehicleTypeCom\":\"21\",\"floatRatioForDouble\":-1,\"standPremium\":3678.75,\"bizPremium\":9932.64,\"insuredDay\":0,\"lastViolationTimes\":-1,\"businessSource\":\"3\",\"insurantName\":\"徐道兵\",\"policyCancelTimes\":0,\"hasInvolvedBizCov\":false,\"lastCompelViolationTimes\":-1,\"forfeitAmount\":-1,\"carOwnerNameSameWithLastPolicy\":true,\"purchasePrice\":299800,\"syxNotChanged\":true,\"carUsage\":\"02\",\"complIssuedEarlyPeriod\":1,\"offInsuredFlag\":true,\"carOwnerIdNo\":\"3****************9\",\"customerType\":\"03\",\"zaCustomerGrade\":-1,\"bizLastClaimTimes\":-1,\"bizSelfRangeMin\":0.65,\"carOwnerCertificateType\":\"I\",\"insureDate\":0,\"offInsuredDay\":0,\"sameLastInsurePlaceFlag\":false,\"isTYCode\":false,\"agentBizTypeLabel\":\"无标签\",\"compelFlag\":true,\"isLoaned\":false,\"insurantPhoneNumUsedTimes\":-1,\"isShortTerm\":false,\"choiceClaimCount\":0,\"claimAmountForce\":0,\"eSignAuthorized\":false,\"noClaimDiscountForce\":1,\"billDate\":20220101,\"energyType\":\"D12\",\"agentExpandWayLabel\":\"无标签\",\"areaCode\":\"\",\"vehicleModelCode\":\"DZAAWI0018\",\"castleSelfChannelRatio\":0,\"carOwnerLastDidiDriverIntervalMonths\":-1,\"absoluteDeductibleRatio\":-1,\"claimTimes\":0,\"jingyouRuleReqDTO\":{\"vehicleClass\":\"轿车类\",\"seatCount\":5},\"annualAverageMileage\":-1,\"frameNo\":\"EW345657654355764\",\"insuredIdNo\":\"3****************9\",\"transferFlag\":false,\"insuredGender\":\"M\",\"bizSelfRangeIn\":\"1.35\",\"insureYears\":0,\"lastYearClaimTimes\":\"0\",\"cityCode\":\"500100\",\"policyHolderIdNo\":\"3****************9\",\"newCarFlag\":true,\"insurantIDUsedTimes\":-1,\"newCarPrice\":299800,\"reinsureFlag\":false,\"vehicleBrand\":\"大众\",\"zaFlowId\":\"DE9BD503-F069-4508-AB5D-3BF3692490F6\",\"paDoubleRatio\":-1,\"predictLossRatio\":0,\"manufactureDate\":0,\"insuredCertificateType\":\"I\",\"businessExpireDate\":20230101,\"shortTermFlag\":false,\"productType\":\"0205\",\"registerDate\":1634832000000,\"newCoverages\":\"\",\"vehicleAge\":3,\"carOwnerName\":\"徐道兵\",\"inputMarginalCostRate\":-1,\"lastBizViolationTimes\":-1,\"familyCode\":\"DZA0AA\",\"plateNo\":\"渝A*\",\"bizIssuedEarlyPeriod\":1,\"forceClaimCount\":0,\"paSelfRatio\":1.35,\"insurantLastDidiDriverIntervalMonths\":-1,\"paRenewalFlag\":false,\"compelDuringSalesPeriod\":true,\"isPaRenewal\":false,\"vehicleStandardName\":\"蔚揽MAGOTAN VP GTE插电式混合动力旅行轿车2019款 混动版\",\"zaDoubleRatio\":-1,\"exhaustMeasure\":\"1.395\",\"paKydFeeRate\":0.279,\"systemCode\":\"1001\",\"userDeviceNo\":\"OGJiOWQxYzVmZjBiMjNhM2IxNGQzZTk2NTBiY2YyZDQtMjZmY2M1Y2U0ZGJlNDhlZWJkZGE0ODRlN2U2NTdmOTcwNTAzLXhYQ0hVSlFPNFFxbFlOaldBSXBGM3BORVJ2ND0=\",\"vehicleSeries\":\"帕萨特PASSAT\",\"bizSelfRatioProduct\":1.35,\"carFeature\":\"11\",\"jyFlag\":false,\"bizSelfRatio\":1.35,\"carOwnerAge\":32,\"noClaimDiscountBiz\":1,\"paDiscountLocked\":false,\"businessType\":\"0\"}";
    JSONObject jsonObject = JSONObject.parseObject(json);
    System.out.println();
  }

  @Test
  public void test1() {
    String str = "";
    Optional.ofNullable(str).ifPresent(e -> System.out.println("jie guo = " + e));
  }

  @Test
  public void testSupplier() {
    HashSet<Object> set = new HashSet<>();
    set.add(1);
    set.add(2);
    HashMap<Object, Object> map = getObj(Maps::newHashMap);
    System.out.println(map);

  }

  public <T> T getObj(Supplier<T> supplier) {
    System.out.println("do some thing");
    return supplier.get();
  }

  // ======== 测试方法引用 ==========
  @Test
  public void testMethodInvoke() {
    Integer result = getInt("10", 2, OtherTest::change);
    System.out.println(result);
  }

  // 被引用的方法
  private static Integer change(String target) {
    return Integer.valueOf(target) + 1;
  }

  private Integer getInt(String target, Integer multi, Function<String, Integer> function) {
    Integer integer = function.apply(target);
    return integer * multi;
  }

  @Test
  public void test() {
    HashMap<Object, Object> map = Maps.newHashMap();
    map.computeIfAbsent("name", (key) -> key + "_v");
    System.out.println(map);
  }

  @Test
  public void testRegex() {
    String src = "http://www.baidu.com/download?bucket=b_value&key2=value2&key3=value3";
    System.out.println(getValue("bucket", src));
    System.out.println(getValue("key2", src));
    System.out.println(getValue("key3", src));

  }

  public String getValue(String key, String src) {
    String regex1 = String.format("%s=(.*?)&", key);
    String regex2 = String.format("%s=(.*)", key);
    String regexValue = getRegexValue(src, regex1);
    return regexValue == null ? getRegexValue(src, regex2) : regexValue;
  }

  public String getRegexValue(String src, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(src);
    boolean existed = matcher.find();
    return existed ? matcher.group(1) : null;
  }

}
