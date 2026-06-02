import sys
import PyPDF2

sys.stdout.reconfigure(encoding='utf-8')

pdf_path = 'd:/Project/BTL-CNPM/BG CNPM 2020.pdf'
reader = PyPDF2.PdfReader(pdf_path)

# Extract and print text of document pages 213, 214, 215 (which correspond to index 212, 213, 214)
for i in [212, 213, 214]:
    print(f"================ PAGE {i+1} ================")
    print(reader.pages[i].extract_text())
