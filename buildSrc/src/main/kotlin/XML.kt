import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.OutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private val builder by lazy { DocumentBuilderFactory.newInstance().newDocumentBuilder() }
private val xpath by lazy { XPathFactory.newInstance().newXPath() }
private val transformer by lazy {
    TransformerFactory.newInstance().newTransformer().apply {
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    }
}

fun String.asDocument(): Document =
        builder.parse(byteInputStream())

fun File.asDocument(): Document =
        builder.parse(this)

fun Document.writeTo(output: OutputStream) =
        transformer.transform(DOMSource(this), StreamResult(output))

fun NodeList.asList(): List<Node> =
        (0 until length).map(::item)

fun Node.evaluate(xPath: String): List<Node> =
        (xpath.evaluate(xPath, this, XPathConstants.NODESET) as NodeList)
                .asList()

fun Element.mergeWith(other: Element) = other
        .childNodes
        .asList()
        .forEach { appendChild(ownerDocument.adoptNode(it.cloneNode(true))) }
