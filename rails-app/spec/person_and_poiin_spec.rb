require "spec_helper"

describe "PersonAndPoiin" do
  before(:each)do
    PersonAndPoiin.delete_all
  end
  it "should find person and poiin ids" do
    person_poiin = PersonAndPoiin.new "user_id"=>1, "poiin_id"=>2, "date" => Time.new.to_i
    person_poiin.save
    retrievd_person_and_poiin = PersonAndPoiin.all({"user_id"=>1,"poiin_id"=>2})
    retrievd_person_and_poiin.should_not be_nil
    retrievd_person_and_poiin.should include person_poiin
  end

    it "should allow deletion of rows when older than half an hour from now" do
    person_poiin = PersonAndPoiin.new "user_id"=>1, "poiin_id"=>2, "date" => (Time.new.to_i - 6000)* 1000
    person_poiin.save
    retrievd_person_and_poiin = PersonAndPoiin.remove_old_poiins(1,2)
    retrievd_person_and_poiin = PersonAndPoiin.all({"user_id"=>1,"poiin_id"=>2})
    retrievd_person_and_poiin.should == []
    end

  it "should allow to filter poiins from a list of poiins" do
    person_poiin = PersonAndPoiin.new "user_id"=>1, "poiin_id"=>2, "date" => (Time.new.to_i - 6000)* 1000
    person_poiin.save
    poiin1 = Poiin.new "id" => 3, "user_id" => 1
    poiin2 = Poiin.new "id" => 2,"user_id" => 1
    retrieved_poiins = PersonAndPoiin.filter_poiins 1, [poiin1,poiin2]
    retrieved_poiins.should include poiin1
    retrieved_poiins.should_not include poiin2
  end

    it "should allow save of collection of poiins for user" do
    poiin1 = Poiin.new "id" => 3, "user_id" => 1
    poiin2 = Poiin.new "id" => 2,"user_id" => 1
    PersonAndPoiin.save(1,[poiin1,poiin2])
    retrieved=PersonAndPoiin.all({"user_id"=>1})
    retrieved.size.should == 2
    retrieved[0]["user_id"].should == 1
    retrieved[0]["poiin_id"].should == 3
    retrieved[1]["user_id"].should == 1
    retrieved[1]["poiin_id"].should == 2
  end
end