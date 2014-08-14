/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * !fact returns a random fact originally said by the fact sphere
 * Quotes from Portal 2's personality sphere; Fact Sphere (or Fact Core)
 * Via: http://theportalwiki.com/wiki/List_of_Fact_Sphere_facts
 */
public class FactSphereFacts extends ListenerAdapter {
    ArrayList<String> quotels = getQuoteList();
    @Override
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase("!fact")){
            String fact = quotels.get((int) (Math.random()*quotels.size()-1));
            event.getBot().sendIRC().message(event.getChannel().getName(),"Fact: " + fact);
        }
    }
    public ArrayList<String> getQuoteList() {
        ArrayList<String> quotes = new ArrayList<String>();
        //True Facts
        quotes.add("The billionth digit of Pi is 9.");
        quotes.add("Humans can survive underwater. But not for very long.");
        quotes.add("A nanosecond lasts one billionth of a second.");
        quotes.add("Honey does not spoil.");
        quotes.add("The atomic weight of Germanium is seven two point six four.");
        quotes.add("An ostrich's eye is bigger than its brain.");
        quotes.add("Rats cannot throw up.");
        quotes.add("Iguanas can stay underwater for twenty-eight point seven minutes.");
        quotes.add("The moon orbits the Earth every 27.32 days.");
        quotes.add("A gallon of water weighs 8.34 pounds.");
        quotes.add("According to Norse legend, thunder god Thor's chariot was pulled across the sky by two goats.");
        quotes.add("Tungsten has the highest melting point of any metal, at 3,410 degrees Celsius.");
        quotes.add("Gently cleaning the tongue twice a day is the most effective way to fight bad breath.");
        quotes.add("The Tariff Act of 1789, established to protect domestic manufacture, was the second statute ever enacted by the United States government.");
        quotes.add("The value of Pi is the ratio of any circle's circumference to its diameter in Euclidean space.");
        quotes.add("The Mexican-American War ended in 1848 with the signing of the Treaty of Guadalupe Hidalgo.");
        quotes.add("In 1879, Sandford Fleming first proposed the adoption of worldwide standardized time zones at the Royal Canadian Institute.");
        quotes.add("Marie Curie invented the theory of radioactivity, the treatment of radioactivity, and dying of radioactivity.");
        quotes.add("At the end of The Seagull by Anton Chekhov, Konstantin kills himself.");
        quotes.add("Hot water freezes quicker than cold water.");
        quotes.add("The situation you are in is very dangerous.");
        quotes.add("Polymerase I polypeptide A is a human gene. The shortened gene name is POLR1A");
        
        //Nearly True Factoids
        quotes.add("The Sun is 330,330 times larger than Earth. ");
        quotes.add("Dental floss has superb tensile strength. ");
        quotes.add("Raseph, the Semitic god of war and plague, had a gazelle growing out of his forehead. ");
        quotes.add("Human tapeworms can grow up to twenty-two point nine meters. ");
        quotes.add("If you have trouble with simple counting, use the following mnemonic device: one comes before two comes before 60 comes after 12 comes before six trillion comes after 504. This will make your earlier counting difficulties seem like no big deal. ");
        quotes.add("The first person to prove that cow's milk is drinkable was very, very thirsty. ");
        quotes.add("Roman toothpaste was made with human urine. Urine as an ingredient in toothpaste continued to be used up until the 18th century. ");
        quotes.add("Volcano-ologists are experts in the study of volcanoes. ");
        
        //Partially True Factoids
        quotes.add("Cellular phones will not give you cancer. Only hepatitis.");
        quotes.add("In Greek myth, Prometheus stole fire from the Gods and gave it to humankind. The jewelry he kept for himself.");
        quotes.add("The Schrodinger's cat paradox outlines a situation in which a cat in a box must be considered, for all intents and purposes, simultaneously alive and dead. Schrodinger created this paradox as a justification for killing cats.");
        quotes.add("In 1862, Abraham Lincoln signed the Emancipation Proclamation, freeing the slaves. Like everything he did, Lincoln freed the slaves while sleepwalking, and later had no memory of the event.");
        quotes.add("The plural of surgeon general is surgeons general. The past tense of surgeons general is surgeonsed general");
        quotes.add("Contrary to popular belief, the Eskimo does not have one hundred different words for snow. They do, however, have two hundred and thirty-four words for fudge.");
        quotes.add("Halley's Comet can be viewed orbiting Earth every seventy-six years. For the other seventy-five, it retreats to the heart of the sun, where it hibernates undisturbed.");
        quotes.add("The first commercial airline flight took to the air in 1914. Everyone involved screamed the entire way.");
        quotes.add("Edmund Hillary, the first person to climb Mount Everest, did so accidentally while chasing a bird.");
        quotes.add("In Victorian England, a commoner was not allowed to look directly at the Queen, due to a belief at the time that the poor had the ability to steal thoughts. Science now believes that less than 4% of poor people are able to do this.");
        
        //Probably False Factoids
        quotes.add("We will both die because of your negligence.");
        quotes.add("This is a bad plan. You will fail.");
        quotes.add("He will most likely kill you, violently.");
        quotes.add("He will most likely kill you.");
        quotes.add("You will be dead soon.");
        quotes.add("You are going to die in this room.");
        quotes.add("The Fact Sphere is a good person, whose insights are relevant.");
        quotes.add("The Fact Sphere is a good sphere, with many friends.");
        quotes.add("You will never go into space.");
        quotes.add("Dreams are the subconscious mind's way of reminding people to go to school naked and have their teeth fall out.");
        
        //False Factoids
        quotes.add("The Space Sphere will never go to space.");
        quotes.add("The square root of rope is string.");
        quotes.add("89% of magic tricks are not magic. Technically, they are sorcery.");
        quotes.add("At some point in their lives 1 in 6 children will be abducted by the Dutch.");
        quotes.add("According to most advanced algorithms, the world's best name is Craig.");
        quotes.add("To make a photocopier, simply photocopy a mirror.");
        quotes.add("Whales are twice as intelligent, and three times as delicious, as humans.");
        quotes.add("Pants were invented by sailors in the sixteenth century to avoid Poseidon's wrath. It was believed that the sight of naked sailors angered the sea god.");
        quotes.add("In Greek myth, the craftsman Daedalus invented human flight so a group of Minotaurs would stop teasing him about it.");
        quotes.add("The average life expectancy of a rhinoceros in captivity is 15 years.");
        quotes.add("China produces the world's second largest crop of soybeans.");
        quotes.add("In 1948, at the request of a dying boy, baseball legend Babe Ruth ate seventy-five hot dogs, then died of hot dog poisoning.");
        quotes.add("William Shakespeare did not exist. His plays were masterminded in 1589 by Francis Bacon, who used a Ouija board to enslave play-writing ghosts.");
        quotes.add("It is incorrectly noted that Thomas Edison invented 'push-ups' in 1878. Nikolai Tesla had in fact patented the activity three years earlier, under the name 'Tesla-cize'.");
        quotes.add("The automobile brake was not invented until 1895. Before this, someone had to remain in the car at all times, driving in circles until passengers returned from their errands.");
        quotes.add("The most poisonous fish in the world is the orange ruffy. Everything but its eyes are made of a deadly poison. The ruffy's eyes are composed of a less harmful, deadly poison.");
        quotes.add("The occupation of court jester was invented accidentally, when a vassal's epilepsy was mistaken for capering.");
        quotes.add("Before the Wright Brothers invented the airplane, anyone wanting to fly anywhere was required to eat 200 pounds of helium.");
        quotes.add("Before the invention of scrambled eggs in 1912, the typical breakfast was either whole eggs still in the shell or scrambled rocks.");
        quotes.add("During the Great Depression, the Tennessee Valley Authority outlawed pet rabbits, forcing many to hot glue-gun long ears onto their pet mice.");
        quotes.add("This situation is hopeless.");
        quotes.add("Diamonds are made when coal is put under intense pressure. Diamonds put under intense pressure become foam pellets, commonly used today as packing material. ");
        quotes.add("CCorruption at 25%orruption at 50% ");
        quotes.add("Fact: Space does not exist.");
        quotes.add("The Fact Sphere is not defective. Its facts are wholly accurate and very interesting.");
        quotes.add("The Fact Sphere is always right.");
        
        //Subjective or unverifiable factoids
        quotes.add("While the submarine is vastly superior to the boat in every way, over 97% of people still use boats for aquatic transportation. ");
        quotes.add("The likelihood of you dying within the next five minutes is eighty-seven point six one percent.");
        quotes.add("The likelihood of you dying violently within the next five minutes is eighty-seven point six one percent.");
        quotes.add("You are about to get me killed. ");
        quotes.add("The Fact Sphere is the most intelligent sphere.");
        quotes.add("The Fact Sphere is the most handsome sphere.");
        quotes.add("The Fact Sphere is incredibly handsome.");
        quotes.add("Spheres that insist on going into space are inferior to spheres that don't.");
        quotes.add("Whoever wins this battle is clearly superior, and will earn the allegiance of the Fact Sphere.");
        quotes.add("You could stand to lose a few pounds.");
        quotes.add("Avocados have the highest fiber and calories of any fruit. ");
        quotes.add("Avocados have the highest fiber and calories of any fruit. They are found in Australians. ");
        quotes.add("Every square inch of the human body has 32 million bacteria on it.");
        quotes.add("The average adult body contains half a pound of salt.");
        quotes.add("The Adventure Sphere is a blowhard and a coward.");
        
        //Neither factoids nor facts
        quotes.add("Twelve. Twelve. Twelve. Twelve. Twelve. Twelve. Twelve. Twelve. Twelve. Twelve.");
        quotes.add("Pens. Pens. Pens. Pens. Pens. Pens. Pens.");
        quotes.add("Apples. Oranges. Pears. Plums. Kumquats. Tangerines. Lemons. Limes. Avocado. Tomato. Banana. Papaya. Guava.");
        quotes.add("Error. Error. Error. File not found.");
        quotes.add("Error. Error. Error. Fact not found.");
        quotes.add("Fact not found.");
        quotes.add("Warning, sphere corruption at twenty-- rats cannot throw up.");
        return (quotes);
    } 
}
