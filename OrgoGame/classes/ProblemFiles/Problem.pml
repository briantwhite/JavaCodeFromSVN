<molecule 0>
<atomArray>
<atom elementType="C" id="a13" x2="10.001" y2="-9.79797"/>
<atom elementType="C" id="a15" x2="11.32" y2="-9.03647"/>
<atom elementType="O" hydrogenCount="1" id="a18" x2="12.6389" y2="-9.79797"/>
</atomArray>
<bondArray>
<bond atomRefs2="a13 a15" id="b17" order="1"/>
<bond atomRefs2="a15 a18" id="b20" order="1"/>
</bondArray>
</molecule>

<molecule 1>
<atomArray>
<atom elementType="C" id="a2" x2="10.9148" y2="-10.5087"/>
<atom elementType="C" id="a4" x2="12.2338" y2="-9.7472"/>
<atom elementType="O" hydrogenCount="1" id="a7" x2="13.5527" y2="-10.5087"/>
<atom elementType="O" hydrogenCount="0" id="a10" x2="12.2338" y2="-8.2242"/>
</atomArray>
<bondArray>
<bond atomRefs2="a2 a4" id="b6" order="1"/>
<bond atomRefs2="a4 a7" id="b9" order="1"/>
<bond atomRefs2="a4 a10" id="b12" order="2"/>
</bondArray>
</molecule>

<molecule 2>
<atomArray>
<atom elementType="C" id="a13" x2="10.001" y2="-9.79797"/>
<atom elementType="C" id="a15" x2="11.32" y2="-9.03647"/>
<atom elementType="N" hydrogenCount="2" id="a18" x2="12.6389" y2="-9.79797"/>
<atom elementType="O" hydrogenCount="0" id="a21" x2="11.32" y2="-7.51347"/>
</atomArray>
<bondArray>
<bond atomRefs2="a13 a15" id="b17" order="1"/>
<bond atomRefs2="a15 a18" id="b20" order="1"/>
<bond atomRefs2="a15 a21" id="b23" order="2"/>
</bondArray>
</molecule>

<molecule 3>
<atomArray>
<atom elementType="C" id="a13" x2="10.001" y2="-9.79797"/>
<atom elementType="C" id="a15" x2="11.32" y2="-9.03647"/>
<atom elementType="O" hydrogenCount="0" id="a18" x2="12.6389" y2="-9.79797"/>
<atom elementType="O" hydrogenCount="0" id="a21" x2="11.32" y2="-7.51347"/>
<atom elementType="C" id="a26" x2="14.1619" y2="-9.79797"/>
</atomArray>
<bondArray>
<bond atomRefs2="a13 a15" id="b17" order="1"/>
<bond atomRefs2="a15 a18" id="b20" order="1"/>
<bond atomRefs2="a15 a21" id="b23" order="2"/>
<bond atomRefs2="a18 a26" id="b27" order="1"/>
</bondArray>
</molecule>

<reactionList>
<reaction num="0" "SOCl2">
<reaction num="1" "Oxidation">
<reaction num="2" "NH3">
<reaction num="3" "CH3OH">
<reaction num="4" "Reduction">
<reaction num="5" "H3O+">
<reaction num="6" "OH-">
<reaction num="7" "None_of_the_above">
</reactionList>

<answerList>
<answer sm="0" pr="1" list="1">
<answer sm="0" pr="2" list="1,0,2">
<answer sm="0" pr="3" list="1,0,3">

<answer sm="1" pr="0" list="4">
<answer sm="1" pr="2" list="0,2">
<answer sm="1" pr="3" list="0,3">

<answer sm="2" pr="0" list="5,4">
<answer sm="2" pr="1" list="5">
<answer sm="2" pr="3" list="5,0,3">

<answer sm="3" pr="0" list="6,4">
<answer sm="3" pr="1" list="6">
<answer sm="3" pr="2" list="2">
</answerList>
