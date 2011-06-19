require "spec_helper"

describe "PersonAndPoiin" do

  it "should create list of these from list of poiins" do
    poiin = Poiin.new :user_id => 123
    poiin2 = Poiin.new :user_id =>999
    person1 = User.new
    person2 = User.new
    list_of_poiins = [poiin, poiin2]
    User.should_receive(:find).with(123).and_return(person1)
    User.should_receive(:find).with(999).and_return(person2)
    list_of_these = PersonAndPoiin.build_list(list_of_poiins)
    list_of_these.size.should == 2
    list_of_these[0].person.should_not be_nil
    list_of_these[0].poiin.should_not be_nil
    list_of_these[1].person.should_not be_nil
    list_of_these[1].poiin.should_not be_nil
  end

  it "should create an object with the person and his poiin" do
    person = User.new
    poiin = Poiin.new
    person_and_poiin = PersonAndPoiin.new(person, poiin)
    person_and_poiin.person.should be person
    person_and_poiin.poiin.should be poiin
  end

end